package di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module
import session.DATA_STORE_FILE_NAME
import session.createDataStore

actual val platformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> {
        createDataStore {
            "${System.getProperty("user.home")}/.vgro/$DATA_STORE_FILE_NAME"
        }
    }
}
