package networking.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val employee: Employee,
    val token: Token
)

@Serializable
data class Employee(
    val id: Int,
    val email: String,
    val firstname: String,
    val lastname: String
)

@Serializable
data class Token(
    @SerialName("access_token")  val accessToken: String,
    @SerialName("token_type")    val tokenType: String,
    @SerialName("expires_in")    val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("created_at")    val createdAt: String
)