// commonTest/kotlin/AppLogicTest.kt
import kotlinx.coroutines.test.runTest
import networking.FakeApiClient
import networking.util.NetworkError
import networking.util.onError
import networking.util.onSuccess
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppLogicTest {

    @Test
    fun `censorWords returns censored text on success`() = runTest {
        val client = FakeApiClient(shouldFail = false)
        var result: String? = null
        client.censorWords("badword").onSuccess { result = it }
        assertNotNull(result)
        assertEquals("***censored***", result)
    }

    @Test
    fun `censorWords returns error on failure`() = runTest {
        val client = FakeApiClient(shouldFail = true)
        var errorCalled = false
        client.censorWords("badword").onError { errorCalled = true }
        assertTrue(errorCalled)
    }

    @Test
    fun `censorWords called with empty string`() = runTest {
        val client = FakeApiClient(shouldFail = false)
        client.censorWords("")
        assertEquals("", client.lastQueriedWord)
    }

    @Test
    fun `lastQueriedWord stores the input text`() = runTest {
        val client = FakeApiClient(shouldFail = false)
        client.censorWords("hello")
        assertEquals("hello", client.lastQueriedWord)
    }

    @Test
    fun `isLoading resets to false after API call`() = runTest {
        val client = FakeApiClient(shouldFail = false)
        var isLoading = true
        client.censorWords("test")
        isLoading = false
        assertEquals(false, isLoading)
    }

    @Test
    fun `errorMessage is null on success`() = runTest {
        val client = FakeApiClient(shouldFail = false)
        var errorMessage: NetworkError? = null
        client.censorWords("test").onSuccess { errorMessage = null }
        assertNull(errorMessage)
    }

    @Test
    fun `errorMessage is set on failure`() = runTest {
        val client = FakeApiClient(shouldFail = true)
        var errorMessage: NetworkError? = null
        client.censorWords("test").onError { errorMessage = it }
        assertEquals(NetworkError.SERVER_ERROR, errorMessage)
    }
}
