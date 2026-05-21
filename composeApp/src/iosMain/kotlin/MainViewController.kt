import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import di.initKoin
import networking.InsultCensorClient
import networking.createHttpClient
import io.ktor.client.engine.darwin.Darwin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App(  client = remember {
        InsultCensorClient(createHttpClient(Darwin.create()))
    },
        prefs = remember {
            createDataStore()
        })
}