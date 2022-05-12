package tech.jacobkgh.internquiz.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import tech.jacobkgh.internquiz.Controller.AppState
import tech.jacobkgh.internquiz.Controller.Screen


@Composable
fun RootQuizView(appState: AppState) {


    when (appState.activeScreen) {
        Screen.LOADING -> {
            loadingComposable()
        }

        Screen.MENU -> {
            QuizMenuComposable(categories = appState.categories)
        }
        Screen.GAME -> {
            if (appState.quizGameSession == null || !appState.quizGameSession!!.isGameReady)
                loadingComposable()
            else
                QuestionView(
                    appState
                )
        }
        Screen.GAMEOVER -> GameOver(quizGameSession = appState.quizGameSession!!)
        Screen.ERROR -> ErrorComposable(message = appState.lastError)
    }
}


@Preview(showBackground = false)
@Composable
fun DefaultPreview() {
    RootQuizView(remember {
        AppState()
    })

}