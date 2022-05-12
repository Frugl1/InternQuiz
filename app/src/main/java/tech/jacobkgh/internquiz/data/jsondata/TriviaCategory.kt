package tech.jacobkgh.internquiz.data.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class TriviaCategory(
    val id: Int,
    val name: String
)