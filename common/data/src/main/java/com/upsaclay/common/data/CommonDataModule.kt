package com.upsaclay.common.data

import com.upsaclay.common.data.local.UserDataStore
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.FCMNotificationSenderImpl
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.data.remote.api.FCMApi
import com.upsaclay.common.data.remote.api.ImageApi
import com.upsaclay.common.data.remote.api.ImageApiImpl
import com.upsaclay.common.data.remote.api.UserFirestoreApi
import com.upsaclay.common.data.remote.api.UserFirestoreApiImpl
import com.upsaclay.common.data.remote.api.UserRetrofitApi
import com.upsaclay.common.data.repository.CredentialsRepositoryImpl
import com.upsaclay.common.data.repository.DrawableRepositoryImpl
import com.upsaclay.common.data.repository.FileRepositoryImpl
import com.upsaclay.common.data.repository.ImageRepositoryImpl
import com.upsaclay.common.data.repository.UserRepositoryImpl
import com.upsaclay.common.domain.FCMNotificationSender
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.CredentialsRepository
import com.upsaclay.common.domain.repository.DrawableRepository
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val GED_SERVER_QUALIFIER = named("server_qualifier")

private val okHttpClient = OkHttpClient.Builder().build()
private val BACKGROUND_SCOPE = named("BackgroundScope")

val commonDataModule = module {
    single<Retrofit>(GED_SERVER_QUALIFIER) {
        Retrofit.Builder()
            .baseUrl("https://gedserver.duckdns.org:3000")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(ImageApiImpl.RetrofitImageApi::class.java)
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(UserRetrofitApi::class.java)
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(FCMApi::class.java)
    }

    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
    SupervisorJob() +
            Dispatchers.IO +
            CoroutineExceptionHandler { coroutineContext, throwable ->
                e("Uncaught error in backgroundScope", throwable)
            }
        )
    }

    singleOf(::DrawableRepositoryImpl) { bind<DrawableRepository>() }
    singleOf(::FileRepositoryImpl) { bind<FileRepository>() }

    singleOf(::ImageApiImpl) { bind<ImageApi>() }
    singleOf(::ImageRepositoryImpl) { bind<ImageRepository>() }
    singleOf(::ImageRemoteDataSource)

    single<UserRepository> {
        UserRepositoryImpl(
            userRemoteDataSource = get(),
            userLocalDataSource = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::UserRemoteDataSource)
    singleOf(::UserLocalDataSource)
    singleOf(::UserDataStore)
    singleOf(::UserFirestoreApiImpl) { bind<UserFirestoreApi>() }

    singleOf(::CredentialsRepositoryImpl) { bind<CredentialsRepository>() }
    singleOf(::FCMNotificationSenderImpl) { bind<FCMNotificationSender>() }
}