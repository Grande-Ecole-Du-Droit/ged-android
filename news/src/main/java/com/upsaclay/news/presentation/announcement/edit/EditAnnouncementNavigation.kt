package com.upsaclay.news.presentation.announcement.edit

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class EditAnnouncementRoute(val announcementId: String)

fun NavController.navigateToEditAnnouncement(announcementId: String) {
    navigate(route = EditAnnouncementRoute(announcementId))
}

fun NavGraphBuilder.editAnnouncementScreen(
    onBackClick: () -> Unit
) {
    composable<EditAnnouncementRoute> {
        val announcementId = it.toRoute<EditAnnouncementRoute>().announcementId

        EditAnnouncementDestination(
            announcementId = announcementId,
            onBackClick = onBackClick
        )
    }
}