package com.upsaclay.message

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.presentation.MessageNotificationPresenter
import com.upsaclay.message.presentation.chat.ChatViewModel
import com.upsaclay.message.presentation.conversation.ConversationViewModel
import com.upsaclay.message.presentation.conversation.create.CreateConversationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val messageModule = module {
    viewModelOf(::ConversationViewModel)
    viewModelOf(::CreateConversationViewModel)
    viewModel { (conversation: Conversation) ->
        ChatViewModel(
            conversation = conversation,
            userRepository = get(),
            conversationRepository = get(),
            messageRepository = get(),
            sendMessageUseCase = get(),
            resendMessageUseCase = get(),
            messageNotificationUseCase = get()
        )
    }
    singleOf(::MessageNotificationPresenter)
}