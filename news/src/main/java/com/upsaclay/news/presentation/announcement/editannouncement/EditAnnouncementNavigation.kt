package com.upsaclay.news.presentation.announcement.editannouncement

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.news.domain.NewsJsonConverter
import com.upsaclay.news.domain.entity.Announcement
import kotlinx.serialization.Serializable

@Serializable data class EditAnnouncementRoute(val announcementJson: String): Route

fun NavController.navigateToEditAnnouncement(announcement: Announcement) {
    navigate(route = EditAnnouncementRoute(NewsJsonConverter.fromAnnouncement(announcement)))
}

fun NavGraphBuilder.editAnnouncementScreen(
    onBackClick: () -> Unit
) {
    composable<EditAnnouncementRoute> { entry ->
        val announcement = entry.toRoute<EditAnnouncementRoute>().announcementJson
            .let { NewsJsonConverter.toAnnouncement(it) } ?: return@composable onBackClick()

        EditAnnouncementScreenRoute(
            announcement = announcement,
            onBackClick = onBackClick
        )
    }
}