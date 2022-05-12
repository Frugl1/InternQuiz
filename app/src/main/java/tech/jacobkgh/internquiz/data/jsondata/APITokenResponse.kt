package tech.jacobkgh.internquiz.data.jsondata

import kotlinx.serialization.Serializable

@Serializable
data class APITokenResponse(
    val response_code: Int,
    val response_message: String,
    val token: String
)