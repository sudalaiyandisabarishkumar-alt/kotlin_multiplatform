// iosTest/kotlin/AppIOSTest.kt
import kotlinx.coroutines.test.runTest
import networking.FakeApiClient
import networking.util.onSuccess
import kotlin.test.Test
import kotlin.test.assertEquals

class AppIOSTest {

    @Test
    fun `iOS - censorWords success`() = runTest {
        val client = FakeApiClient(shouldFail = false)
        var result: String? = null

        client.censorWords("badword")
            .onSuccess { result = it }

        assertEquals("***censored***", result)
    }
}
