package dependencies

import android.content.Context

actual class DbClient(
    private val context: Context
)

actual fun getDeviceName(): String {
    return "Android"
}