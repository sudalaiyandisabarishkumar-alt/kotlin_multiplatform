package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import dev.icerock.moko.permissions.compose.BindEffect

fun MainViewController() = ComposeUIViewController {
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val permissionsController = factory.createPermissionsController()
    BindEffect(permissionsController)
    App(permissionsController = permissionsController)
}