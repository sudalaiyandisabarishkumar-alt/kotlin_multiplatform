package org.example.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PermissionScreen(viewModel: PermissionViewModel) {
    val locationGranted by viewModel.locationGranted.collectAsState()
    val cameraGranted by viewModel.cameraGranted.collectAsState()
    val notificationGranted by viewModel.notificationGranted.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        PermissionRow(
            label = "📍 Location",
            granted = locationGranted,
            onRequest = { viewModel.requestLocationPermission() }
        )
        PermissionRow(
            label = "📷 Camera",
            granted = cameraGranted,
            onRequest = { viewModel.requestCameraPermission() }
        )
        PermissionRow(
            label = "🔔 Notifications",
            granted = notificationGranted,
            onRequest = { viewModel.requestNotificationPermission() }
        )
    }
}

@Composable
fun PermissionRow(label: String, granted: Boolean, onRequest: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = label)
        if (granted) {
            Text("✅ Granted", color = Color.Green)
        } else {
            Button(onClick = onRequest) { Text("Request") }
        }
    }
}