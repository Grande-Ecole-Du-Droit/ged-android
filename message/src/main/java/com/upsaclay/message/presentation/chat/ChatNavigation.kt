package com.upsaclay.message.presentation.chat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.message.domain.MessageJsonConverter
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoute(val conversationJson: String): Route {
    companion object {
        const val NAME = "ChatRoute"
        const val CONVERSATION_JSON_ARGUMENT = "conversationJson"
    }
}

fun NavController.navigateToChat(
    conversation: Conversation,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ChatRoute(MessageJsonConverter.toConversationJson(conversation))) {
        navOptions()
    }
}

fun NavController.navigateToChat(
    conversationMessageJson: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = ChatRoute(conversationMessageJson)) {
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