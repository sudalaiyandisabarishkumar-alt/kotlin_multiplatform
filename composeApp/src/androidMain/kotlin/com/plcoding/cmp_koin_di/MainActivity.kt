package com.plcoding.cmp_koin_di

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient
import androidx.compose.runtime.remember
import createDataStore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                client = remember {
                    InsultCensorClient(createHttpClient(OkHttp.create()))
                },
                prefs = remember {
                    createDataStore(applicationContext)
                }
            )
        }
    }
}

