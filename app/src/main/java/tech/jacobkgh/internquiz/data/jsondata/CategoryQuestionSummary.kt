package tech.jacobkgh.internquiz.data.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class CategoryQuestionSummary(
    val category_id: Int,
    val category_question_count: CategoryQuestionCount
)