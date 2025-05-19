package com.upsaclay.authentication.data

import com.upsaclay.authentication.data.api.FirebaseAuthenticationApi
import com.upsaclay.authentication.data.api.FirebaseAuthenticationApiImpl
import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.repository.AuthenticationRepositoryImpl
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepository
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepositoryImpl
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.e
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val authenticationDataModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
    SupervisorJob() +
            Dispatchers.IO +
            CoroutineExceptionHandler { coroutineContext, throwable ->
                e("Uncaught error in backgroundScope", throwable)
            }
        )
    }

    singleOf(::FirebaseAuthenticationRepositoryImpl) { bind<FirebaseAuthenticationRepository>() }
    singleOf(::FirebaseAuthenticationApiImpl) { bind<FirebaseAuthenticationApi>() }

    single<AuthenticationRepository> {
        AuthenticationRepositoryImpl(
            firebaseAuthenticationRepository = get(),
            authenticationLocalDataSource = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::AuthenticationLocalDataSource)
}