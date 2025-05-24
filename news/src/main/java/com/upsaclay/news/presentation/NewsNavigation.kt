package com.upsaclay.news.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.news.presentation.news.NewsScreenRoute
import kotlinx.serialization.Serializable

@Serializable data object NewsBaseRoute
@Serializable data object NewsRoute

fun NavController.navigateToNews(navOptions: NavOptions? = null) {
    navigate(route = NewsRoute, navOptions = navOptions)
}

fun NavGraphBuilder.newsSection(
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onProfilePictureClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    newsDestination: NavGraphBuilder.() -> Unit
) {
    navigation<NewsBaseRoute>(startDestination = NewsRoute) {
        composable<NewsRoute> {
            NewsScreenRoute(
                onAnnouncementClick = onAnnouncementClick,
                onCreateAnnouncementClick = onCreateAnnouncementClick,
                onProfilePictureClick = onProfilePictureClick,
                bottomBar = bottomBar
            )
        }
    }
    newsDestination()
}