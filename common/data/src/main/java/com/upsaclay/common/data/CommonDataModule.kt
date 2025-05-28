package com.upsaclay.common.data

import com.upsaclay.common.data.local.UserDataStore
import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.NotificationApiImpl
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.data.remote.api.ImageApi
import com.upsaclay.common.data.remote.api.ImageApiImpl
import com.upsaclay.common.data.remote.api.UserFirestoreApi
import com.upsaclay.common.data.remote.api.UserFirestoreApiImpl
import com.upsaclay.common.data.remote.api.UserRetrofitApi
import com.upsaclay.common.data.remote.api.WhiteListApi
import com.upsaclay.common.data.repository.CredentialsRepositoryImpl
import com.upsaclay.common.data.repository.DrawableRepositoryImpl
import com.upsaclay.common.data.repository.FileRepositoryImpl
import com.upsaclay.common.data.repository.ImageRepositoryImpl
import com.upsaclay.common.data.repository.UserRepositoryImpl
import com.upsaclay.common.data.repository.WhiteListRepositoryImpl
import com.upsaclay.common.domain.NotificationApi
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.CredentialsRepository
import com.upsaclay.common.domain.repository.DrawableRepository
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.repository.WhiteListRepository
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
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                Dispatchers.IO +
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    e("Uncaught error in backgroundScope", throwable)
                }
        )
    }

    single<Retrofit>(GED_SERVER_QUALIFIER) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
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
            .create(FcmApi::class.java)
    }

    single {
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(WhiteListApi::class.java)
    }

    singleOf(::ImageApiImpl) { bind<ImageApi>() }
    singleOf(::UserFirestoreApiImpl) { bind<UserFirestoreApi>() }

    singleOf(::NotificationApiImpl) { bind<NotificationApi>() }
    singleOf(::ImageRemoteDataSource)
    singleOf(::UserRemoteDataSource)

    singleOf(::UserLocalDataSource)
    singleOf(::UserDataStore)

    singleOf(::CredentialsRepositoryImpl) { bind<CredentialsRepository>() }
    singleOf(::DrawableRepositoryImpl) { bind<DrawableRepository>() }
    singleOf(::FileRepositoryImpl) { bind<FileRepository>() }
    singleOf(::ImageRepositoryImpl) { bind<ImageRepository>() }
    single<UserRepository> {
        UserRepositoryImpl(
            userRemoteDataSource = get(),
            userLocalDataSource = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::WhiteListRepositoryImpl) { bind<WhiteListRepository>() }
}