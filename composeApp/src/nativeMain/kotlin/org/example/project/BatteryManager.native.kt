package org.example.project
import platform.UIKit.UIDevice

actual class BatteryManager {
    actual fun getBatteryLevel(): Int {
        UIDevice.currentDevice.batteryMonitoringEnabled=true
        val batteryLevel=UIDevice.currentDevice.batteryLevel
        return (batteryLevel*100).toInt()
    }
}