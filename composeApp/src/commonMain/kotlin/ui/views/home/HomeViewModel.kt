package ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import networking.ApiClient
import networking.models.DashboardResponse
import session.PreferencesRepository
import networking.util.onSuccess

class HomeViewModel(
    private val apiClient: ApiClient,
    private val prefsRepo: PreferencesRepository,
) : ViewModel() {

    val firstName: StateFlow<String> = prefsRepo.firstName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val lastName: StateFlow<String> = prefsRepo.lastName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    var dashboardResponse by mutableStateOf<DashboardResponse?>(null)
    var isRefreshing      by mutableStateOf(false)

    init { loadDashboard() }

    fun refresh() = loadDashboard()

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            prefsRepo.clearSession()
            onComplete()
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            isRefreshing = true
            val token = prefsRepo.accessToken.first()
            if (token.isNotBlank()) {
                apiClient.getDashboard(token)
                    .onSuccess { dashboardResponse = it }
            }
            isRefreshing = false
        }
    }
}
