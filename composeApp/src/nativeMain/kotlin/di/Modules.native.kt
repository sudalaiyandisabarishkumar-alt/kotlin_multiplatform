@file:OptIn(ExperimentalForeignApi::class)

package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import session.DATA_STORE_FILE_NAME
import session.createDataStore

actual val platformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single<DataStore<Preferences>> {
        createDataStore {
            val dir = NSFileManager.defaultManager.URLForDirectory(
                directory         = NSDocumentDirectory,
                inDomain          = NSUserDomainMask,
                appropriateForURL = null,
                create            = false,
                error             = null,
            )
            requireNotNull(dir).path + "/$DATA_STORE_FILE_NAME"
        }
    }
}
