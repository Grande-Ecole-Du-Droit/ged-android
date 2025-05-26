package com.upsaclay.message.presentation.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.message.domain.MessageJsonConverter
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.serialization.Serializable

@Serializable data class ChatRoute(val conversationJson: String)

fun NavController.navigateToChat(
    conversation: Conversation,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ChatRoute(MessageJsonConverter.fromConversation(conversation))) {
        navOptions()
    }
}

fun NavGraphBuilder.chatScreen(
    onBackClick: () -> Unit
) {
    composable<ChatRoute> { entry ->
        val conversation = entry.toRoute<ChatRoute>().conversationJson
            .let { MessageJsonConverter.toConversation(it) } ?: return@composable onBackClick()

        ChatScreenRoute(
            conversation = conversation,
            onBackClick = onBackClick
        )
    }
}