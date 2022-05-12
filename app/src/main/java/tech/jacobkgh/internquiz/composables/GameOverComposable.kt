package tech.jacobkgh.internquiz.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import tech.jacobkgh.internquiz.Controller.QuizGameSession

@Composable
fun GameOver(quizGameSession: QuizGameSession) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column(verticalArrangement = Arrangement.SpaceEvenly) {


            Text(
                text = "We're all out questions for this category :(",
                fontSize = 28.sp,
                color = Color.White
            )
            Text(
                text = "You answered a total of ${quizGameSession.totalAnswers} questions.",
                fontSize = 24.sp,
                color = Color.White
            )
            Text(
                text = "${quizGameSession.correctAnswers} of your answers were correct!",
                fontSize = 24.sp,
                color = Color.White

            )
            Text(
                text = "Your longest answer streak was ${quizGameSession.longestStreak}.",
                fontSize = 24.sp,
                color = Color.White
            )

        }

    }
}