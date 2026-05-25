package networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import networking.models.DashboardResponse
import networking.models.LoginResponse
import networking.util.NetworkError
import networking.util.Result
import networking.util.toResult

private const val BASE_URL     = "https://api.uat.velichamgrow.com/api/v1"
private const val BUILD_NUMBER = 626

class ApiClient(private val httpClient: HttpClient) {

    suspend fun login(
        employeeId: String,
        password:   String,
    ): Result<LoginResponse, NetworkError> {
        val response = try {
            httpClient.post("$BASE_URL/user_management/employee/login") {
                contentType(ContentType.Application.Json)
                setBody(buildJsonObject {
                    putJsonObject("employee") {
                        put("email",        employeeId)
                        put("password",     password)
                        put("build_number", BUILD_NUMBER)
                        put("is_mobile",    true)
                        put("grant_type",   "password")
                    }
                })
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return response.toResult { body<LoginResponse>() }
    }

    suspend fun getDashboard(accessToken: String): Result<DashboardResponse, NetworkError> {
        val response = try {
            httpClient.get("$BASE_URL/dashboard_management/dashboards") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $accessToken")
            }
        } catch (e: UnresolvedAddressException) {
            return Result.Error(NetworkError.NO_INTERNET)
        } catch (e: SerializationException) {
            return Result.Error(NetworkError.SERIALIZATION)
        }
        return response.toResult { body<DashboardResponse>() }
    }
}
