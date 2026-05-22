// commonTest/kotlin/networking/FakeInsultCensorClient.kt
package networking

import util.NetworkError
import util.Result

// ✅ No inheritance — standalone fake
class FakeInsultCensorClient(
    private val shouldFail: Boolean = false
) {
    var lastCensoredWord: String? = null

    suspend fun censorWords(uncensored: String): Result<String, NetworkError> {
        lastCensoredWord = uncensored
        return if (shouldFail) {
            Result.Error(NetworkError.SERVER_ERROR)
        } else {
            Result.Success("***censored***")
        }
    }
}