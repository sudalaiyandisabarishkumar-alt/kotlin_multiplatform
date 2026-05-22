import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.plcoding.nativeiosincompose.NativeViewFactory
import di.initKoin
import networking.InsultCensorClient
import networking.createHttpClient
import io.ktor.client.engine.darwin.Darwin

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("No view factory provided.")
}

fun MainViewController(
    nativeViewFactory: NativeViewFactory
) = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {                                          // ✅ content lambda opened
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        App(
            client = remember {
                InsultCensorClient(createHttpClient(Darwin.create()))
            },
            prefs = remember {
                createDataStore()
            }
        )
    }
}                                            // ✅ lambda closed