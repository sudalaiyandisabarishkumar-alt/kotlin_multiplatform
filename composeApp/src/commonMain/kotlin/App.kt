@file:OptIn(KoinExperimentalAPI::class)

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.first
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.app.ui.LoginScreen
import kotlinx.coroutines.launch
import networking.InsultCensorClient
import org.koin.compose.KoinContext
import org.koin.core.annotation.KoinExperimentalAPI
import util.NetworkError
import util.onError
import util.onSuccess

// ─── Route constants ──────────────────────────────────────────────────────────
private object Routes {
    const val LOGIN           = "login"
    const val FORGOT_PASSWORD = "forgot_password/{employeeId}"
    const val HOME            = "home"

    fun forgotPassword(employeeId: String) = "forgot_password/$employeeId"
}

// ─── App entry-point ──────────────────────────────────────────────────────────
// ─── App entry-point ──────────────────────────────────────────────────────────
@Composable
fun App(client: InsultCensorClient, prefsRepo: PreferencesRepository) {
    MaterialTheme {
        KoinContext {
            val navController  = rememberNavController()
            val scope          = rememberCoroutineScope()

            var dashboardResponse  by remember { mutableStateOf<DashboardResponse?>(null) }
            var isDashboardLoading by remember { mutableStateOf(false) }

            // ✅ null = still checking, "" = not logged in, else = logged in
            var startDestination by remember { mutableStateOf<String?>(null) }

            suspend fun loadDashboard(token: String) {
                isDashboardLoading = true
                client.getDashboard(token)
                    .onSuccess { dashboardResponse = it }
                    .onError   { }
                isDashboardLoading = false
            }

            // ✅ Check saved token on every app open
            LaunchedEffect(Unit) {
                val token = prefsRepo.accessToken.first()
                if (token.isNotEmpty()) {
                    loadDashboard(token)              // load dashboard with saved token
                    startDestination = Routes.HOME    // go to home
                } else {
                    startDestination = Routes.LOGIN   // no token → login
                }
            }

            // ✅ Show splash/loading until we know where to go
            if (startDestination == null) {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()
                }
                return@KoinContext
            }

            NavHost(
                navController    = navController,
                startDestination = startDestination!!,   // ✅ LOGIN or HOME
            ) {

                // ── Login ─────────────────────────────────────────────────────
                composable(Routes.LOGIN) {
                    var isLoading    by remember { mutableStateOf(false) }
                    var errorMessage by remember { mutableStateOf<String?>(null) }

                    LoginScreen(
                        isLoading    = isLoading,
                        errorMessage = errorMessage,
                        onLoginSuccess = { employeeId, password ->
                            scope.launch {
                                isLoading    = true
                                errorMessage = null
                                try {
                                    client.login(employeeId, password)
                                        .onSuccess { loginResponse ->
                                            prefsRepo.saveLoginSession(loginResponse)
                                            client.getDashboard(loginResponse.token.accessToken)
                                                .onSuccess { dashboard ->
                                                    dashboardResponse = dashboard
                                                    navController.navigate(Routes.HOME) {
                                                        popUpTo(Routes.LOGIN) { inclusive = true }
                                                    }
                                                }
                                                .onError { err ->
                                                    errorMessage = "Dashboard: ${err.name}"
                                                }
                                        }
                                        .onError { err ->
                                            errorMessage = err.name
                                        }
                                } catch (e: Exception) {
                                    errorMessage = "Unexpected error: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        onForgotPassword = { navController.navigate(Routes.forgotPassword(it)) },
                        onTermsTap       = { },
                    )
                }

                // ── Home ──────────────────────────────────────────────────────
                composable(Routes.HOME) {
                    val firstName by prefsRepo.firstName.collectAsState(initial = "")
                    val lastName  by prefsRepo.lastName.collectAsState(initial = "")

                    HomeScreen(
                        firstName         = firstName,
                        lastName          = lastName,
                        dashboardResponse = dashboardResponse,
                        isRefreshing      = isDashboardLoading,
                        onRefresh         = {
                            scope.launch {
                                val token = prefsRepo.accessToken.first()
                                loadDashboard(token)
                            }
                        },
                        onLogout = {
                            scope.launch {
                                prefsRepo.clearSession()
                                dashboardResponse = null
                                startDestination  = Routes.LOGIN  // ✅ reset for next time
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