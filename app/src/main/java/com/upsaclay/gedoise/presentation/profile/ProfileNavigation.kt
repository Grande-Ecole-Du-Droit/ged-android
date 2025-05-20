package com.upsaclay.gedoise.presentation.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable data object ProfileBaseRoute
@Serializable data object ProfileRoute

fun NavController.navigateToProfile(navOptions: NavOptions? = null) {
    navigate(route = ProfileBaseRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profileSection(
    onAccountClick: () -> Unit,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    profileDestination: NavGraphBuilder.() -> Unit
) {
    navigation<ProfileBaseRoute>(startDestination = ProfileRoute) {
        composable<ProfileRoute> {
            ProfileDestination(
                onAccountClick = onAccountClick,
                onBackClick = onBackClick,
                bottomBar = bottomBar
            )
        }
        profileDestination()
    }
}