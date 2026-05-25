package com.plcoding.cmp_koin_di

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient
import createDataStore
import PreferencesRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val prefsRepo = remember {
                PreferencesRepository(createDataStore(applicationContext))
            }

            App(
                client    = remember {
                    InsultCensorClient(createHttpClient(OkHttp.create()))
                },
                prefsRepo = prefsRepo
            )
        }
    }
}