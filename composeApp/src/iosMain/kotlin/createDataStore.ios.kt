@file:OptIn(ExperimentalForeignApi::class)

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun createDataStore(): DataStore<Preferences> {
    return createDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        println("nsdkd $DATA_STORE_FILE_NAME")

        val fullPath = requireNotNull(directory).path + "/$DATA_STORE_FILE_NAME"

        println("nsdsdkd $fullPath")

        fullPath  // return this as the last line
    }
}