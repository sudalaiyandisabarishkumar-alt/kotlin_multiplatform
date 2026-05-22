@file:OptIn(KoinExperimentalAPI::class)

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator

import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import cmp_koin_di.composeapp.generated.resources.Res
import cmp_koin_di.composeapp.generated.resources.charger
import cmp_koin_di.composeapp.generated.resources.compose_multiplatform
import dependencies.DbClient
import dependencies.MyViewModel
import networking.InsultCensorClient
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import util.NetworkError
import util.onError
import util.onSuccess
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.plcoding.nativeiosincompose.NativeButton

@Composable
@Preview
fun App(client: InsultCensorClient, prefs: DataStore<Preferences>){
    MaterialTheme {
        val savedText by prefs
            .data
            .map {
                val textKey = stringPreferencesKey("counter")
                it[textKey]
            }
            .collectAsState(initial = null)

        var censoredText by remember(savedText) {
            mutableStateOf<String?>(savedText)
        }
        var uncensoredText by remember {
            mutableStateOf("")
        }
        var isLoading by remember {
            mutableStateOf(false)
        }
        var errorMessage by remember {
            mutableStateOf<NetworkError?>(null)
        }
        val scope = rememberCoroutineScope()
        KoinContext {
            NavHost(
                navController = rememberNavController(),
                startDestination = "home"
            ) {
                composable(route = "home") {
                    val viewModel = koinViewModel<MyViewModel>()
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TextField(
                                value = uncensoredText,
                                onValueChange = { uncensoredText = it },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                placeholder = {
                                    Text("Uncensored text")
                                }
                            )// In App.kt
                            key(isLoading) {  // ← forces NativeButton to recreate when isLoading changes
                                Box(modifier = Modifier.testTag("censor_button")) {
                                    NativeButton(onClick = {
                                        scope.launch {
                                            try {
                                                isLoading = true
                                                errorMessage = null
                                                client.censorWords(uncensoredText)
                                                    .onSuccess {
                                                        prefs.edit { dataStore ->
                                                            val textKey =
                                                                stringPreferencesKey("counter")
                                                            dataStore[textKey] = it
                                                        }
                                                    }
                                                    .onError { errorMessage = it }
                                            } catch (e: Exception) {
                                                println("CRASH: ${e.message}")
                                            } finally {
                                                isLoading = false
                                            }
                                        }
                                    }, isLoading)
                                }
                            }// ← isLoading passed here triggers createButtonView on change
                            savedText?.let {
                                Text(it)  // always reads from DataStore
                            }
                            errorMessage?.let {
                                Text(
                                    text = it.name,
                                    color = Color.Red
                                )
                            }

//                            Image(
//                                painter = painterResource(Res.drawable.charger),
//                                contentDescription = "My Image",
//                                modifier = Modifier.fillMaxWidth()
//                            )
                            Text(
                                text = viewModel.getHelloWorldString() // "Hello World!"
                            )
                            Text(
                                text = viewModel.getDeviceName()       // "Android Samsung" or "iPhone 14"
                            )
                        }
                    }
                }
            }
        }
    }
}