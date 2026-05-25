package networking.util

import io.ktor.client.statement.HttpResponse

suspend fun <T> HttpResponse.toResult(onSuccess: suspend HttpResponse.() -> T): Result<T, NetworkError> =
    when (status.value) {
        in 200..299 -> Result.Success(onSuccess())
        401         -> Result.Error(NetworkError.UNAUTHORIZED)
        408         -> Result.Error(NetworkError.REQUEST_TIMEOUT)
        409         -> Result.Error(NetworkError.CONFLICT)
        413         -> Result.Error(NetworkError.PAYLOAD_TOO_LARGE)
        in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
        else        -> Result.Error(NetworkError.UNKNOWN)
    }
