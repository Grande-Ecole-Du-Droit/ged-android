package com.upsaclay.gedoise.presentation.profile.account

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object AccountRoute: Route

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