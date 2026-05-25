package networking

import networking.util.NetworkError
import networking.util.Result

// Standalone fake — no inheritance needed
class FakeApiClient(
    private val shouldFail: Boolean = false,
) {
    var lastQueriedWord: String? = null

    // Kept for tests that verify result-handling patterns
    suspend fun censorWords(uncensored: String): Result<String, NetworkError> {
        lastQueriedWord = uncensored
        return if (shouldFail) Result.Error(NetworkError.SERVER_ERROR)
        else                   Result.Success("***censored***")
    }
}
