package tech.jacobkgh.internquiz.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.jacobkgh.internquiz.Controller.AppState
import tech.jacobkgh.internquiz.Controller.QuizGameSession

@Composable
fun QuestionView(
    appState: AppState,
) {

    val quizGameSession: QuizGameSession = appState.quizGameSession!!

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Log.d("Quiz", "View recomposed!")
        Text(
            quizGameSession.currentQuestion.questionText,
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 15.dp)
        )
        Spacer(modifier = Modifier.height(200.dp))
        Column(
            modifier = Modifier.padding(bottom = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            for (i in quizGameSession.currentQuestion.possibleAnswers.indices) {
                Button(
                    onClick = { quizGameSession.submitAnswer(i) },
                    modifier = Modifier
                        .background(if (quizGameSession.isShowingCorrectAnswer && quizGameSession.currentQuestion.correctIndex == i) Color.Green else Color.Gray)
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(quizGameSession.currentQuestion.possibleAnswers[i], fontSize = 18.sp)
                }

            }
            Text(
                text = "Score ${quizGameSession.correctAnswers}",
                fontSize = 18.sp,
                color = Color.White
            )
        }

    }

}


@Preview(showBackground = true)
@Composable
fun QuestionPreviewPreview() {
    // QuestionView ("PLACEHOLDER QUESTION", listOf("Første svar", "Andet Svar", "Tredje Svar"), null)
}