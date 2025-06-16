package com.upsaclay.message.data

import com.upsaclay.common.domain.e
import com.upsaclay.message.data.local.ConversationLocalDataSource
import com.upsaclay.message.data.local.ConversationMessageLocalDataSource
import com.upsaclay.message.data.local.MessageLocalDataSource
import com.upsaclay.message.data.remote.ConversationRemoteDataSource
import com.upsaclay.message.data.remote.MessageRemoteDataSource
import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.api.ConversationApiImpl
import com.upsaclay.message.data.remote.api.MessageApi
import com.upsaclay.message.data.remote.api.MessageApiImpl
import com.upsaclay.message.data.repository.ConversationMessageRepositoryImpl
import com.upsaclay.message.data.repository.ConversationRepositoryImpl
import com.upsaclay.message.data.repository.MessageRepositoryImpl
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val BACKGROUND_SCOPE = named("BackgroundScope")

val messageDataModule = module {
    single<CoroutineScope>(BACKGROUND_SCOPE) {
        CoroutineScope(
            SupervisorJob() +
                    Dispatchers.IO +
                    CoroutineExceptionHandler { _, throwable ->
                        e("Uncaught error in backgroundScope", throwable)
                    }
        )
    }

    singleOf(::ConversationApiImpl) { bind<ConversationApi>() }
    singleOf(::ConversationRemoteDataSource)
    singleOf(::ConversationLocalDataSource)

    singleOf(::ConversationRepositoryImpl) { bind<ConversationRepository>() }
    single<ConversationMessageRepository> {
        ConversationMessageRepositoryImpl(
            conversationMessageLocalDataSource = get(),
            scope = get(BACKGROUND_SCOPE)
        )
    }

    singleOf(::ConversationMessageLocalDataSource)

    singleOf(::MessageRepositoryImpl) { bind<MessageRepository>() }
    singleOf(::MessageApiImpl) { bind<MessageApi>() }
    singleOf(::MessageRemoteDataSource)
    singleOf(::MessageLocalDataSource)
}