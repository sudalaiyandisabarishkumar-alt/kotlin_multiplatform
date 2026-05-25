import App
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.window.ComposeUIViewController
import com.plcoding.nativeiosincompose.NativeViewFactory
import di.initKoin

val LocalNativeViewFactory = staticCompositionLocalOf<NativeViewFactory> {
    error("No NativeViewFactory provided.")
}

fun MainViewController(
    nativeViewFactory: NativeViewFactory,
) = ComposeUIViewController(
    configure = { initKoin() }
) {
    CompositionLocalProvider(LocalNativeViewFactory provides nativeViewFactory) {
        App()
    }
}
