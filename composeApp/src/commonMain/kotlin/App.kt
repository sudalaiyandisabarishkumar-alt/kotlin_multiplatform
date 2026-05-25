@file:OptIn(KoinExperimentalAPI::class)

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import session.PreferencesRepository
import ui.home.HomeScreen
import ui.home.HomeViewModel
import ui.login.LoginScreen
import ui.login.LoginViewModel

// ─── Route constants ──────────────────────────────────────────────────────────
private object Routes {
    const val LOGIN           = "login"
    const val HOME            = "home"
    const val FORGOT_PASSWORD = "forgot_password/{employeeId}"

    fun forgotPassword(id: String) = "forgot_password/$id"
}

// ─── App entry-point (all deps injected via Koin) ─────────────────────────────
@Composable
fun App() {
    MaterialTheme {
        KoinContext {
            val prefsRepo: PreferencesRepository = koinInject()

            // null = DataStore still loading from disk (< 100 ms)
            val accessToken by prefsRepo.accessToken.collectAsState(initial = null)

            if (accessToken == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                return@KoinContext
            }

            val startDestination = if (accessToken!!.isNotBlank()) Routes.HOME else Routes.LOGIN
            val navController    = rememberNavController()

            NavHost(navController = navController, startDestination = startDestination) {

                // ── Login ──────────────────────────────────────────────────────
                composable(Routes.LOGIN) {
                    val viewModel: LoginViewModel = koinViewModel()
                    LoginScreen(
                        isLoading    = viewModel.isLoading,
                        errorMessage = viewModel.errorMessage,
                        onLoginSuccess = { id, pw ->
                            viewModel.login(id, pw) {
                                navController.navigate(Routes.HOME) {
                                    popUpTo(Routes.LOGIN) { inclusive = true }
                                }
                            }
                        },
                        onForgotPassword = { navController.navigate(Routes.forgotPassword(it)) },
                    )
                }

                // ── Home ───────────────────────────────────────────────────────
                composable(Routes.HOME) {
                    val viewModel: HomeViewModel = koinViewModel()
                    val firstName by viewModel.firstName.collectAsState()
                    val lastName  by viewModel.lastName.collectAsState()
                    HomeScreen(
                        firstName         = firstName,
                        lastName          = lastName,
                        dashboardResponse = viewModel.dashboardResponse,
                        isRefreshing      = viewModel.isRefreshing,
                        onRefresh         = viewModel::refresh,
                        onLogout          = {
                            viewModel.logout {
                                navController.navigate(Routes.LOGIN) {
                                    popUpTo(Routes.HOME) { inclusive = true }
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
