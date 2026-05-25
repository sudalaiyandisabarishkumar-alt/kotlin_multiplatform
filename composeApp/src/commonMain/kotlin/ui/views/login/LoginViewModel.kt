package ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import networking.ApiClient
import session.PreferencesRepository
import networking.util.NetworkError
import networking.util.onError
import networking.util.onSuccess

class LoginViewModel(
    private val apiClient:  ApiClient,
    private val prefsRepo:  PreferencesRepository,
) : ViewModel() {

    var isLoading    by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun login(employeeId: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            isLoading    = true
            errorMessage = null
            try {
                apiClient.login(employeeId, password)
                    .onSuccess { response ->
                        prefsRepo.saveLoginSession(response)
                        onSuccess()
                    }
                    .onError { err: NetworkError ->
                        errorMessage = err.name
                    }
            } catch (e: Exception) {
                errorMessage = "Unexpected error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
