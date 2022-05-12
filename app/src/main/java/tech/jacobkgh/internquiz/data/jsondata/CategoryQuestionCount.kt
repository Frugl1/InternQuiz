package tech.jacobkgh.internquiz.data.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class CategoryQuestionCount(
    val total_easy_question_count: Int,
    val total_hard_question_count: Int,
    val total_medium_question_count: Int,
    val total_question_count: Int
)