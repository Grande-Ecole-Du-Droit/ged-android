package com.upsaclay.gedoise.presentation.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable data object ProfileRoute

fun NavController.navigateToProfile() {
    navigate(route = ProfileRoute)
}

fun NavGraphBuilder.profileScreen(
    onAccountClick: () -> Unit,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    composable<ProfileRoute> {
        ProfileScreenRoute(
            onAccountClick = onAccountClick,
            onBackClick = onBackClick,
            bottomBar = bottomBar
        )
    }
}