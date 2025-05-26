package com.upsaclay.news.presentation.announcement.read

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.serialization.Serializable

@Serializable data class ReadAnnouncementRoute(val announcementId: String)

fun NavController.navigateToReadAnnouncement(announcementId: String) {
    navigate(route = ReadAnnouncementRoute(announcementId))
}

fun NavGraphBuilder.readAnnouncementScreen(
    onBackClick: () -> Unit,
    onEditClick: (Announcement) -> Unit
) {
    composable<ReadAnnouncementRoute> {
        val announcementId = it.toRoute<ReadAnnouncementRoute>().announcementId
        ReadAnnouncementScreenRoute(
            announcementId = announcementId,
            onBackClick = onBackClick,
            onEditClick = onEditClick
        )
    }
}