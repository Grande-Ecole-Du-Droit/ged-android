package com.upsaclay.authentication.data

import com.upsaclay.authentication.data.api.FirebaseAuthenticationApi
import com.upsaclay.authentication.data.api.FirebaseAuthenticationApiImpl
import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.repository.AuthenticationRepositoryImpl
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepository
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepositoryImpl
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authenticationDataModule = module {
    singleOf(::FirebaseAuthenticationRepositoryImpl) { bind<FirebaseAuthenticationRepository>() }
    singleOf(::FirebaseAuthenticationApiImpl) { bind<FirebaseAuthenticationApi>() }
    singleOf(::AuthenticationRepositoryImpl) { bind<AuthenticationRepository>() }
    singleOf(::AuthenticationLocalDataSource)
}