package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.compose.BindEffect

@Composable
fun App(permissionsController: PermissionsController) {
    BindEffect(permissionsController)
    val viewModel = remember { PermissionViewModel(permissionsController) }
    PermissionScreen(viewModel = viewModel)
}