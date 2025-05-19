package com.upsaclay.news.presentation.announcement.read

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable data class ReadAnnouncementRoute(val announcementId: String)

fun NavController.navigateToReadAnnouncement(announcementId: String) {
    navigate(route = ReadAnnouncementRoute(announcementId))
}

fun NavGraphBuilder.readAnnouncementScreen(
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit
) {
    composable<ReadAnnouncementRoute> {
        val announcementId = it.toRoute<ReadAnnouncementRoute>().announcementId
        ReadAnnouncementDestination(
            announcementId = announcementId,
            onBackClick = onBackClick,
            onEditClick = onEditClick
        )
    }
}