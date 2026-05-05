package org.example.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.location.LOCATION
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.DeniedAlwaysException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PermissionViewModel(
    private val permissionsController: PermissionsController
) : ViewModel() {

    private val _locationGranted = MutableStateFlow(false)
    val locationGranted: StateFlow<Boolean> = _locationGranted

    private val _cameraGranted = MutableStateFlow(false)
    val cameraGranted: StateFlow<Boolean> = _cameraGranted

    private val _notificationGranted = MutableStateFlow(false)
    val notificationGranted: StateFlow<Boolean> = _notificationGranted

    fun requestLocationPermission() {
        viewModelScope.launch { _locationGranted.value = request(Permission.LOCATION) }
    }

    fun requestCameraPermission() {
        viewModelScope.launch { _cameraGranted.value = request(Permission.CAMERA) }
    }

    fun requestNotificationPermission() {
        viewModelScope.launch { _notificationGranted.value = request(Permission.REMOTE_NOTIFICATION) }
    }

    private suspend fun request(permission: Permission): Boolean {
        return withContext(Dispatchers.Main) {  // ← force Main thread for state update
            try {
                permissionsController.providePermission(permission)
                true
            } catch (e: DeniedAlwaysException) {
                permissionsController.openAppSettings()
                false
            } catch (e: DeniedException) {
                false
            }
        }
    }
}