package com.upsaclay.news.domain

import com.upsaclay.common.domain.e
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RecreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import com.upsaclay.news.domain.usecase.UpdateAnnouncementUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val newsDomainModule = module {
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
        CreateAnnouncementUseCase(
            announcementRepository = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::DeleteAnnouncementUseCase)
    single {
        RecreateAnnouncementUseCase(
            announcementRepository = get(),
            connectivityObserver = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }
    singleOf(::RefreshAnnouncementUseCase)
    singleOf(::UpdateAnnouncementUseCase)
}