package com.plcoding.nativeiosincompose

import platform.UIKit.UIViewController

interface NativeViewFactory {
    fun createButtonView(
        label: String,
        isLoading: Boolean,
        onClick: () -> Unit,
    ): UIViewController
}