package tech.jacobkgh.internquiz.model

import android.text.Html
import kotlin.random.Random

/**
 * Represents a question, that can be used by the quiz game.
 * Supports strings with html entities
 */
class QuizQuestion(
    correctAnswer: String,
    incorrectAnswers: List<String>,
    questionText: String


) {
    val questionText = Html.fromHtml(questionText, Html.FROM_HTML_MODE_COMPACT).toString()
    val correctAnswer = Html.fromHtml(correctAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
    val possibleAnswers = mutableListOf<String>()


    var correctIndex = 0
        private set

    init {
        //build answer list
        for (incorrectAnswer in incorrectAnswers) {
            possibleAnswers.add(
                Html.fromHtml(incorrectAnswer, Html.FROM_HTML_MODE_COMPACT).toString()
            )
        }


        correctIndex = Random.nextInt(
            incorrectAnswers.count()
        )
        possibleAnswers.add(correctIndex, this.correctAnswer)


    }
}