package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.icerock.moko.permissions.PermissionsController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissionsController = PermissionsController(applicationContext = applicationContext)
        permissionsController.bind(this)
        setContent {
            App(permissionsController = permissionsController)
        }
    }
}