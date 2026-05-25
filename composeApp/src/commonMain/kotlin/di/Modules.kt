package di

import io.ktor.client.engine.HttpClientEngine
import networking.ApiClient
import networking.util.createHttpClient
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import session.PreferencesRepository
import ui.home.HomeViewModel
import ui.login.LoginViewModel

expect val platformModule: Module

val sharedModule = module {
    // Network
    single { createHttpClient(get<HttpClientEngine>()) }
    singleOf(::ApiClient)

    // Session
    singleOf(::PreferencesRepository)

    // ViewModels
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
}
