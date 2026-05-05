package di

import dependencies.DbClient
import dependencies.MyRepository
import dependencies.MyRepositoryImpl
import android.os.Build
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

// ✅ No need for AndroidRepositoryImpl
// MyRepositoryImpl handles everything
// just provide actual getDeviceName()

actual val platformModule = module {
    single { DbClient(androidContext()) }
    singleOf(::MyRepositoryImpl).bind<MyRepository>() // ✅ use shared impl
}