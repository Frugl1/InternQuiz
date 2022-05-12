package tech.jacobkgh.internquiz.Controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.jacobkgh.internquiz.data.QuizDataFacade
import tech.jacobkgh.internquiz.model.QuizCategory
import tech.jacobkgh.internquiz.model.QuizQuestion
import tech.jacobkgh.internquiz.model.enumeration.QuizDifficulty
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.max
import kotlin.math.min

/**
 * Class responsible for quiz game logic
 *
 * @property appState refference to the main  AppState
 * @property quizCategory Which category to quiz the player in.
 * @property difficulty The difficulty of the quiz
 * @property questionsPerApiCall how many questions to fetch per "page"
 * @property timeBetweenQuestions delay between submitting an answer and the next one being shown in ms
 * @property onGameOver callback that gets executed when the question queue is empty
 */
class QuizGameSession(
    val appState: AppState,
    val quizCategory: QuizCategory,
    val difficulty: QuizDifficulty,
    var questionsPerApiCall: Int = 5,
    val timeBetweenQuestions: Long = 2000,
    val onGameOver: (() -> Unit)?
) {
    //Exposed for composition
    var totalAnswers by mutableStateOf(0)
        private set
    var correctAnswers by mutableStateOf(0)
        private set
    var correctStreak by mutableStateOf(0)
        private set
    var longestStreak by mutableStateOf(0)
        private set
    var isGameReady by mutableStateOf(false)
        private set
    var isShowingCorrectAnswer by mutableStateOf(false)
        private set
    var isGameOver by mutableStateOf(false)
        private set

    private var questionQueue = mutableListOf<QuizQuestion>()

    private var questionsLeftApi = 0;

    private var processAnswers = true
    private var apiToken: String? = null

    var currentQuestion by mutableStateOf(
        QuizQuestion(
            "PLACEHOLDER",
            listOf("PLACEHOLDER", "PLACEHOLDER", "PLACEHOLDER"),
            "QUESTION PLACEHOLDER"
        )
    )
        private set

    init {

        questionsLeftApi = when (difficulty) {
            QuizDifficulty.EASY -> quizCategory.easyQuestions
            QuizDifficulty.MEDIUM -> quizCategory.mediumQuestions
            QuizDifficulty.HARD -> quizCategory.hardQuestions
        }

        try {


            renewApiToken(onComplete = {

                fillQuestionQueue(onComplete = {
                    nextQuestion()
                    isGameReady = true
                }, onError = { appState.raiseCriticalError("Failed to init quiz") })
            }, onError = { appState.raiseCriticalError("Failed to refresh token") })
        } catch (exception: Exception) {
            appState.raiseCriticalError("Error While initializing quiz :(")
        }
    }


    fun submitAnswer(answerIndex: Int) {
        if (!processAnswers) return
        totalAnswers++
        if (answerIndex == currentQuestion.correctIndex) {
            correctAnswers++
            correctStreak++
            longestStreak = max(longestStreak, correctStreak)
        } else correctStreak = 0;


        isShowingCorrectAnswer = true
        processAnswers = false

        Timer().schedule(timeBetweenQuestions) {
            isShowingCorrectAnswer = false;
            processAnswers = true
            nextQuestion()
        }

    }

    /**
     * Show the next question
     * initiaties re-filling of the question queue if appropriate
     */
    private fun nextQuestion() {
        if (questionQueue.count() == 0) {
            isGameOver = true
            onGameOver?.invoke()
            return
        }
        if (questionQueue.count() < 8)
            fillQuestionQueue()

        currentQuestion = questionQueue.removeFirst()
    }

    /**
     * Gets a new API-token, used to avoid duplicate questions being retrived
     *
     * @param onComplete callback executed when a new new apiToken is has been set
     * @param onError called in case of failure to renew the token
     */
    private fun renewApiToken(
        onComplete: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                apiToken = QuizDataFacade.getAPIToken()
                onComplete?.invoke()
            } catch (exception: Exception) {
                onError?.invoke()
            }
        }
    }

    /**
     * Fetches quetions to the question queue
     *
     * @param amountToFetch amount of questions to be feteched, should not exceed the amount of questions in the API
     * @param onComplete Callback executed when the queue has been filled
     * @param onError Callback executed, should the request fail.
     */
    private fun fillQuestionQueue(
        amountToFetch: Int = questionsPerApiCall,
        onComplete: (() -> Unit)? = null,
        onError: (() -> Unit)? = null
    ) {
        if (questionsLeftApi <= 0) return
        GlobalScope.launch(Dispatchers.IO) {
            try {

                val newQuestions =
                    QuizDataFacade.getQuestions(
                        min(amountToFetch, questionsLeftApi),
                        quizCategory.categoryId,
                        difficulty,
                        apiToken
                    )
                withContext(Dispatchers.Main) {
                    questionQueue.addAll(newQuestions)
                    questionsLeftApi -= amountToFetch
                    onComplete?.invoke()
                }
            } catch (exception: Exception) {
                onError?.invoke()

            }
        }

    }

}