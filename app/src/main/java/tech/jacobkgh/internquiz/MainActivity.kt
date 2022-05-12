package tech.jacobkgh.internquiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import tech.jacobkgh.internquiz.Controller.AppState
import tech.jacobkgh.internquiz.composables.RootQuizView


var appState = AppState()
    private set

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            Box(Modifier.background(Color.Black)) {
                RootQuizView(remember { appState })
            }
        }
    }

    override fun onBackPressed() {
        appState.onBackPressed(this)
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RootQuizView(remember { AppState() })
}