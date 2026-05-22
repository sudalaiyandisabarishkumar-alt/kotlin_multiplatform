// commonTest/kotlin/AppLogicTest.kt
import kotlinx.coroutines.test.runTest
import networking.FakeInsultCensorClient
import util.NetworkError
import util.onSuccess
import util.onError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertNull

class AppLogicTest {

    // ✅ Test 1: success response
    @Test
    fun `censorWords returns censored text on success`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = false)
        var result: String? = null

        client.censorWords("badword").onSuccess { result = it }

        assertNotNull(result)
        assertEquals("***censored***", result)
    }

    // ✅ Test 2: error response
    @Test
    fun `censorWords returns error on failure`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = true)
        var errorCalled = false

        client.censorWords("badword").onError { errorCalled = true }

        assertTrue(errorCalled)
    }

    // ✅ Test 3: empty input
    @Test
    fun `censorWords called with empty string`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = false)

        client.censorWords("")

        assertEquals("", client.lastCensoredWord)
    }

    // ✅ Test 4: lastCensoredWord is tracked correctly
    @Test
    fun `lastCensoredWord stores the input text`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = false)

        client.censorWords("hello")

        assertEquals("hello", client.lastCensoredWord)
    }

    // ✅ Test 5: isLoading resets after call
    @Test
    fun `isLoading resets to false after API call`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = false)
        var isLoading = false

        isLoading = true
        client.censorWords("test")
        isLoading = false

        assertEquals(false, isLoading)
    }

    // ✅ Test 6: error message is null on success
    @Test
    fun `errorMessage is null on success`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = false)
        var errorMessage: NetworkError? = null

        client.censorWords("test").onSuccess {
            errorMessage = null
        }

        assertNull(errorMessage)
    }

    // ✅ Test 7: error message set on failure
    @Test
    fun `errorMessage is set on failure`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = true)
        var errorMessage: NetworkError? = null

        client.censorWords("test").onError {
            errorMessage = it
        }

        assertEquals(NetworkError.SERVER_ERROR, errorMessage)
    }
}