package com.upsaclay.news.presentation.announcement.create

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable data object CreateAnnouncementRoute

fun NavController.navigateToCreateAnnouncement() {
    navigate(route = CreateAnnouncementRoute)
}

fun NavGraphBuilder.createAnnouncementScreen(
    onBackClick: () -> Unit,
) {
    composable<CreateAnnouncementRoute> {
        CreateAnnouncementDestination(
            onBackClick = onBackClick
        )
    }
}