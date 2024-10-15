package com.upsaclay.common.data

import com.upsaclay.common.data.local.UserDataStore
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.data.remote.api.UserFirestoreApiImpl
import com.upsaclay.common.data.repository.DrawableRepositoryImpl
import com.upsaclay.common.data.repository.FileRepositoryImpl
import com.upsaclay.common.data.repository.ImageRepositoryImpl
import com.upsaclay.common.data.repository.UserRepositoryImpl
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val SERVER_1_RETROFIT_QUALIFIER = "server_1_qualifier"
const val SERVER_2_RETROFIT_QUALIFIER = "server_2_qualifier"

private val okHttpClient = OkHttpClient.Builder().build()

val commonDataModule = module {
    single<Retrofit>(qualifier = named(SERVER_1_RETROFIT_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_1_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<Retrofit>(qualifier = named(SERVER_2_RETROFIT_QUALIFIER)) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_2_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(qualifier = named(SERVER_1_RETROFIT_QUALIFIER)).create(com.upsaclay.common.data.remote.api.ImageApi::class.java)
    }

    single {
        get<Retrofit>(
            qualifier = named(SERVER_1_RETROFIT_QUALIFIER)
        ).create(com.upsaclay.common.data.remote.api.UserRetrofitApi::class.java)
    }

    singleOf(::DrawableRepositoryImpl) { bind<com.upsaclay.common.domain.repository.DrawableRepository>() }
    singleOf(::FileRepositoryImpl) { bind<com.upsaclay.common.domain.repository.FileRepository>() }

    singleOf(::ImageRepositoryImpl) { bind<com.upsaclay.common.domain.repository.ImageRepository>() }
    singleOf(::ImageRemoteDataSource)

    singleOf(::UserRepositoryImpl) { bind<com.upsaclay.common.domain.repository.UserRepository>() }
    singleOf(::UserRemoteDataSource)
    singleOf(::UserLocalDataSource)
    singleOf(::UserDataStore)
    singleOf(::UserFirestoreApiImpl) { bind<com.upsaclay.common.data.remote.api.UserFirestoreApi>() }
}