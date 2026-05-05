package di

import dependencies.DbClient
import dependencies.MyRepository
import dependencies.MyRepositoryImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    singleOf(::DbClient)
    singleOf(::MyRepositoryImpl).bind<MyRepository>() // ✅ use shared impl
}