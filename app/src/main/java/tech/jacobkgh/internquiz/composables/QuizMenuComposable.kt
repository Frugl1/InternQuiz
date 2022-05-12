package tech.jacobkgh.internquiz.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tech.jacobkgh.internquiz.appState
import tech.jacobkgh.internquiz.model.QuizCategory
import tech.jacobkgh.internquiz.model.enumeration.QuizDifficulty

@Composable
fun loadingComposable() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = "LOADING", fontSize = 28.sp, color = Color.White)

    }
}

@Composable
fun QuizMenuComposable(categories: List<QuizCategory>) {
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()

    ) {
        Text("Select a category", color = Color.White)
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier
                .height(200.dp)
                .width(345.dp)
                .background(color = Color.LightGray)
        ) {
            items(categories) { item ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = if (selectedCategory == item) Color.Yellow else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    //  Text(text = item.categoryName, fontSize = 16.sp)
                    ClickableText(
                        text = AnnotatedString(item.categoryName),
                        style = TextStyle(fontSize = 18.sp)
                    ) { _ ->
                        selectedCategory = item;
                    }
                }
            }
        }
        Row() {
            Button(onClick = { appState.requestQuizStart(selectedCategory, QuizDifficulty.EASY) }) {
                Text("Easy(${selectedCategory.easyQuestions})")
            }
            Button(
                onClick = { appState.requestQuizStart(selectedCategory, QuizDifficulty.MEDIUM) },
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text("Medium(${selectedCategory.mediumQuestions})")
            }
            Button(onClick = {
                appState.requestQuizStart(
                    selectedCategory,
                    QuizDifficulty.HARD
                )
            }) {
                Text("Hard(${selectedCategory.hardQuestions})")
            }
        }

    }
}

