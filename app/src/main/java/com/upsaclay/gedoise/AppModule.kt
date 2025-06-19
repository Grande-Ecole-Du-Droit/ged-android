package com.upsaclay.gedoise

import MainViewModel
import androidx.room.Room
import com.upsaclay.common.ConnectivityObserverImpl
import com.upsaclay.common.data.GED_SERVER_QUALIFIER
import com.upsaclay.common.data.local.FcmDataStore
import com.upsaclay.common.data.local.FcmLocalDataSource
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.IntentHelper
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.gedoise.data.GedoiseDatabase
import com.upsaclay.gedoise.data.WorkerLauncher
import com.upsaclay.gedoise.data.repository.RouteRepositoryImpl
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.FcmTokenUseCase
import com.upsaclay.gedoise.domain.usecase.ListenDataUseCase
import com.upsaclay.gedoise.domain.usecase.ListenRemoteUserUseCase
import com.upsaclay.gedoise.presentation.navigation.NavigationViewModel
import com.upsaclay.gedoise.presentation.profile.ProfileViewModel
import com.upsaclay.gedoise.presentation.profile.account.AccountViewModel
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
        get<Retrofit>(GED_SERVER_QUALIFIER)
            .create(FcmApi::class.java)
    }

    single { get<GedoiseDatabase>().announcementDao() }
    single { get<GedoiseDatabase>().conversationDao() }
    single { get<GedoiseDatabase>().messageDao() }
    single { get<GedoiseDatabase>().conversationMessageDao() }

    single<ConnectivityObserver> {
        ConnectivityObserverImpl(
            context = androidContext(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::RouteRepositoryImpl) { bind<RouteRepository>() }
    singleOf(::FcmLocalDataSource)
    singleOf(::FcmDataStore)

    viewModelOf(::NavigationViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::MainViewModel)

    singleOf(::ClearDataUseCase)
    singleOf(::ListenDataUseCase)
    single {
        FcmTokenUseCase(
            userRepository = get(),
            credentialsRepository = get(),
            authenticationRepository = get(),
            connectivityObserver = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    single {
        ListenRemoteUserUseCase(
            userRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    singleOf(::IntentHelperImpl) { bind<IntentHelper>() }

    single { WorkerLauncher(context = androidContext()) }
}