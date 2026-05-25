package session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import networking.models.LoginResponse

class PreferencesRepository(private val dataStore: DataStore<Preferences>) {

    val firstName: Flow<String> = dataStore.data.map { it[AppPreferences.FIRST_NAME]  ?: "" }
    val lastName:  Flow<String> = dataStore.data.map { it[AppPreferences.LAST_NAME]   ?: "" }
    val accessToken: Flow<String> = dataStore.data.map { it[AppPreferences.ACCESS_TOKEN] ?: "" }

    suspend fun saveLoginSession(loginResponse: LoginResponse) {
        dataStore.edit { ds ->
            ds[AppPreferences.ACCESS_TOKEN]  = loginResponse.token.accessToken
            ds[AppPreferences.REFRESH_TOKEN] = loginResponse.token.refreshToken
            ds[AppPreferences.SESSION_USER]  = loginResponse.employee.email
            ds[AppPreferences.FIRST_NAME]    = loginResponse.employee.firstname
            ds[AppPreferences.LAST_NAME]     = loginResponse.employee.lastname
        }
    }

    suspend fun clearSession() {
        dataStore.edit { ds ->
            ds.remove(AppPreferences.ACCESS_TOKEN)
            ds.remove(AppPreferences.REFRESH_TOKEN)
            ds.remove(AppPreferences.SESSION_USER)
            ds.remove(AppPreferences.FIRST_NAME)
            ds.remove(AppPreferences.LAST_NAME)
        }
    }
}