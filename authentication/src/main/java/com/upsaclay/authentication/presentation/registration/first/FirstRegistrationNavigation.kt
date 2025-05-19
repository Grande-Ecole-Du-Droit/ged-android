package com.upsaclay.authentication.presentation.registration.first

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable data object FirstRegistrationRoute

fun NavController.navigateToFirstRegistration() {
    navigate(route = FirstRegistrationRoute)
}

fun NavGraphBuilder.firstRegistrationScreen(
    onBackClick: () -> Unit,
    onNextClick: (String, String) -> Unit
) {
    composable<FirstRegistrationRoute> {
        FirstRegistrationRoute(
            onBackClick = onBackClick,
            onNextClick = onNextClick
        )
    }
}
