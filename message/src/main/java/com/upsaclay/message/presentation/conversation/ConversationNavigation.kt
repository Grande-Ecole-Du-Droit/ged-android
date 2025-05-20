package com.upsaclay.message.presentation.conversation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.serialization.Serializable

@Serializable data object ConversationBaseRoute
@Serializable data object ConversationRoute

fun NavController.navigateToConversation(navOptions: NavOptions? = null) {
    navigate(route = ConversationBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.conversationSection(
    onConversationClick: (Conversation) -> Unit,
    onCreateConversation: () -> Unit,
    bottomBar: @Composable () -> Unit,
    messageDestination: NavGraphBuilder.() -> Unit
) {
    navigation<ConversationBaseRoute>(startDestination = ConversationRoute) {
        composable<ConversationRoute> {
            ConversationDestination(
                onConversationClick = onConversationClick,
                onCreateConversation = onCreateConversation,
                bottomBar = bottomBar
            )
        }
        messageDestination()
    }
}

