package com.upsaclay.gedoise

import android.app.Application
import com.upsaclay.authentication.authenticationModule
import com.upsaclay.authentication.data.authenticationDataModule
import com.upsaclay.authentication.domain.authenticationDomainModule
import com.upsaclay.common.data.commonDataModule
import com.upsaclay.common.domain.commonDomainModule
import com.upsaclay.gedoise.domain.usecase.FCMTokenUseCase
import com.upsaclay.message.data.messageDataModule
import com.upsaclay.message.domain.messageDomainModule
import com.upsaclay.message.messageModule
import com.upsaclay.news.data.newsDataModule
import com.upsaclay.news.domain.newsDomainModule
import com.upsaclay.news.newsModule
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.Forest.plant

class GedApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GedApplication)
            modules(
                listOf(
                    appModule,
                    authenticationModule,
                    authenticationDomainModule,
                    authenticationDataModule,
                    commonDomainModule,
                    commonDataModule,
                    newsModule,
                    newsDomainModule,
                    newsDataModule,
                    messageModule,
                    messageDomainModule,
                    messageDataModule
                )
            )
        }

        get<FCMTokenUseCase>().listenEvents()
        plant(Timber.DebugTree())
    }
}