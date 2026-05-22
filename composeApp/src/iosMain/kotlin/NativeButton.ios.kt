package com.plcoding.nativeiosincompose

import LocalNativeViewFactory
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitViewController
import androidx.compose.ui.unit.dp
import kotlinx.cinterop.ExperimentalForeignApi


@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun NativeButton(onClick: () -> Unit, isLoading: Boolean, modifier: Modifier) {
    val factory = LocalNativeViewFactory.current
    UIKitViewController(
        modifier = modifier
            .width(100.dp)
            .height(50.dp),
        factory = {
            factory.createButtonView(
                label = "IOS Button",
                onClick = onClick,
                isLoading = isLoading
            )
        }
    )
}