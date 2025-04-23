package com.upsaclay.gedoise

import androidx.room.Room
import com.upsaclay.common.AndroidConnectivityObserver
import com.upsaclay.common.data.SERVER_1_RETROFIT_QUALIFIER
import com.upsaclay.common.domain.e
import com.upsaclay.gedoise.data.GedoiseDatabase
import com.upsaclay.common.data.remote.api.FCMApi
import com.upsaclay.common.data.local.FCMDataStore
import com.upsaclay.common.data.local.FCMLocalDataSource
import com.upsaclay.gedoise.data.repository.ScreenRepositoryImpl
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.gedoise.domain.repository.ScreenRepository
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.StartListeningDataUseCase
import com.upsaclay.gedoise.domain.usecase.StopListeningDataUseCase
import com.upsaclay.gedoise.domain.usecase.FCMTokenUseCase
import com.upsaclay.gedoise.presentation.NotificationPresenter
import com.upsaclay.gedoise.presentation.viewmodels.AccountViewModel
import com.upsaclay.gedoise.presentation.viewmodels.MainViewModel
import com.upsaclay.gedoise.presentation.viewmodels.NavigationViewModel
import com.upsaclay.gedoise.presentation.viewmodels.ProfileViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private const val DATABASE_NAME = "GedoiseDatabase"
private val BACKGROUND_SCOPE = named("BackgroundScope")

val appModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { coroutineContext, throwable ->
                        e("Uncaught error in backgroundScope", throwable)
                    }
        )
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            GedoiseDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    single {
        get<Retrofit>(qualifier = named(SERVER_1_RETROFIT_QUALIFIER))
            .create(FCMApi::class.java)
    }

    single { get<GedoiseDatabase>().announcementDao() }
    single { get<GedoiseDatabase>().conversationDao() }
    single { get<GedoiseDatabase>().messageDao() }
    single { get<GedoiseDatabase>().conversationMessageDao() }

    singleOf(::NotificationPresenter)

    viewModelOf(::NavigationViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::MainViewModel)

    singleOf(::ClearDataUseCase)
    singleOf(::StartListeningDataUseCase)
    singleOf(::StopListeningDataUseCase)

    singleOf(::ScreenRepositoryImpl) { bind<ScreenRepository>() }
    singleOf(::FCMLocalDataSource)
    singleOf(::FCMDataStore)
    single {
        FCMTokenUseCase(
            userRepository = get(),
            credentialsRepository = get(),
            authenticationRepository = get(),
            connectivityObserver = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    singleOf(::AndroidConnectivityObserver) { bind<ConnectivityObserver>() }
}