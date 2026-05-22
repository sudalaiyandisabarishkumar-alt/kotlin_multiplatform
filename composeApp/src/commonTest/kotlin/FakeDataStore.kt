// commonTest/kotlin/FakeDataStore.kt
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeDataStore : DataStore<Preferences> {

    private val _data = MutableStateFlow(mutablePreferencesOf())

    override val data: Flow<Preferences> = _data

    override suspend fun updateData(
        transform: suspend (Preferences) -> Preferences
    ): Preferences {
        val updated = transform(_data.value)
        _data.update { updated as androidx.datastore.preferences.core.MutablePreferences }
        return updated
    }
}