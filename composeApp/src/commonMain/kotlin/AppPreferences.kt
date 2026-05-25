import androidx.datastore.preferences.core.stringPreferencesKey

object AppPreferences {
    val ACCESS_TOKEN  = stringPreferencesKey("access_token")
    val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    val SESSION_USER  = stringPreferencesKey("session_user")
    val FIRST_NAME    = stringPreferencesKey("first_name")
    val LAST_NAME     = stringPreferencesKey("last_name")
}