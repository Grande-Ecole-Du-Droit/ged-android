package com.upsaclay.authentication.domain

import com.upsaclay.authentication.domain.usecase.LoginUseCase
import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authenticationDomainModule = module {
    singleOf(::LoginUseCase)
    singleOf(::RegisterUseCase)
}