package com.upsaclay.news.presentation.announcement.createannouncement

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object CreateAnnouncementRoute: Route

fun NavController.navigateToCreateAnnouncement() {
    navigate(route = CreateAnnouncementRoute)
}

fun NavGraphBuilder.createAnnouncementScreen(
    onBackClick: () -> Unit,
) {
    composable<CreateAnnouncementRoute> {
        CreateAnnouncementScreenRoute(
            onBackClick = onBackClick
        )
    }
}