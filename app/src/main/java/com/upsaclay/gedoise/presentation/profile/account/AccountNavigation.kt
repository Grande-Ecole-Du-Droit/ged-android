package com.upsaclay.gedoise.presentation.profile.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable data object AccountRoute

fun NavController.navigateToAccount() {
    navigate(route = AccountRoute)
}

fun NavGraphBuilder.accountScreen(
    onBackClick: () -> Unit
) {
    composable<AccountRoute> {
        AccountScreenRoute(
            onBackClick = onBackClick
        )
    }
}