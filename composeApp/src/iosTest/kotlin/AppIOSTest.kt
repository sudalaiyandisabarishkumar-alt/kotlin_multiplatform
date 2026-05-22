// iosTest/kotlin/AppIOSTest.kt
import networking.FakeInsultCensorClient
import util.onSuccess
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppIOSTest {

    @Test
    fun `iOS - censorWords success`() = runTest {
        val client = FakeInsultCensorClient(shouldFail = false)
        var result: String? = null

        client.censorWords("badword")
            .onSuccess { result = it }

        assertEquals("***censored***", result)
    }
}