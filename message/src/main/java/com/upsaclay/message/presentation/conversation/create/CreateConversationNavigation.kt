package com.upsaclay.message.presentation.conversation.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.serialization.Serializable

@Serializable data object CreateConversationRoute

fun NavController.navigateToCreateConversation() {
    navigate(route = CreateConversationRoute)
}

fun NavGraphBuilder.createConversationScreen(
    onBackClick: () -> Unit,
    onCreateConversationClick: (Conversation) -> Unit
) {
    composable<CreateConversationRoute> {
        CreateConversationDestination(
            onBackClick = onBackClick,
            onCreateConversationClick = onCreateConversationClick
        )
    }
}