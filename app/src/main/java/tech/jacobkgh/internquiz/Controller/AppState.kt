package tech.jacobkgh.internquiz.Controller

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.jacobkgh.internquiz.MainActivity
import tech.jacobkgh.internquiz.data.QuizDataFacade
import tech.jacobkgh.internquiz.model.QuizCategory
import tech.jacobkgh.internquiz.model.enumeration.QuizDifficulty

enum class Screen {
    LOADING,
    MENU,
    GAME,
    GAMEOVER,
    ERROR

}

class AppState() {
    lateinit var categories: List<QuizCategory>
        private set
    var quizGameSession: QuizGameSession? = null
        private set
    var activeScreen by mutableStateOf(Screen.LOADING)
        private set
    var lastError by mutableStateOf("UNKONOWN ERROR")

    init {
        GlobalScope.launch(Dispatchers.IO) {
            try {


                val downloadedCategories = QuizDataFacade.getQuizCategories()
                withContext(Dispatchers.Main) {
                    categories = downloadedCategories
                    activeScreen = Screen.MENU
                }
            } catch (exception: Exception) {
                raiseCriticalError("Error loading categories")
            }
        }
    }


    /**
     * Start a quiz session and change the active screen to the game view
     *
     * @param category Category to start a gam in
     * @param difficulty  the difficulty of the quiz
     */
    fun requestQuizStart(category: QuizCategory, difficulty: QuizDifficulty) {
        quizGameSession = QuizGameSession(
            appState = this,
            quizCategory = category,
            difficulty = difficulty,
            onGameOver = this::onGameOver
        )
        activeScreen = Screen.GAME
    }

    /**
     *Tells the appstate to show an error message
     *
     * @param message The message to display
     */
    fun raiseCriticalError(message: String) {
        lastError = message
        activeScreen = Screen.ERROR
    }

    private fun onGameOver() {
        activeScreen = Screen.GAMEOVER
    }

    fun returnToMenu() {
        activeScreen = Screen.MENU
        quizGameSession = null
    }

    fun onBackPressed(activity: MainActivity) {
        when (activeScreen) {
            Screen.GAME -> {
                returnToMenu()
            }
            Screen.GAMEOVER -> {
                returnToMenu()
            }
            else -> {
                activity.finish()
            }
        }
    }
}

