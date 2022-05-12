package tech.jacobkgh.internquiz.data.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class QuizCategories(
    val trivia_categories: List<TriviaCategory>
)