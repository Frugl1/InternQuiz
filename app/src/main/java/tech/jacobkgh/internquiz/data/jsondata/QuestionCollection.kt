package tech.jacobkgh.internquiz.data.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class QuestionCollection(
    val response_code: Int,
    val results: List<Result>
)