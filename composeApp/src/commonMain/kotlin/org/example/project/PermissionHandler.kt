package org.example.project

import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.location.LOCATION
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.DeniedAlwaysException

class PermissionHandler(val controller: PermissionsController) {

    suspend fun requestLocation(): Boolean = request(Permission.LOCATION)
    suspend fun requestCamera(): Boolean = request(Permission.CAMERA)
    suspend fun requestNotification(): Boolean = request(Permission.REMOTE_NOTIFICATION)

    private suspend fun request(permission: Permission): Boolean {
        return try {
            controller.providePermission(permission)
            true
        } catch (e: DeniedAlwaysException) {
            controller.openAppSettings()
            false
        } catch (e: DeniedException) {
            false
        }
    }
}