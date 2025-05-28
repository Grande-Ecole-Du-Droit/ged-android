package com.upsaclay.gedoise.presentation.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object ProfileRoute: Route

fun NavController.navigateToProfile() {
    navigate(route = ProfileRoute)
}

fun NavGraphBuilder.profileScreen(
    onAccountClick: () -> Unit,
    onBackClick: () -> Unit
) {
    composable<ProfileRoute> {
        ProfileScreenRoute(
            onAccountClick = onAccountClick,
            onBackClick = onBackClick
        )
    }
}