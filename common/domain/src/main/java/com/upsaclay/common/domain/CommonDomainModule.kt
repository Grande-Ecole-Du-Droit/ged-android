package com.upsaclay.common.domain

import com.upsaclay.common.domain.usecase.DeleteProfilePictureUseCase
import com.upsaclay.common.domain.usecase.SharedEventsUseCase
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonDomainModule = module {
    singleOf(::DeleteProfilePictureUseCase)
    singleOf(::SharedEventsUseCase)
    singleOf(::UpdateProfilePictureUseCase)
}