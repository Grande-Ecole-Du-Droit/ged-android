package com.upsaclay.authentication.presentation.registration.firstregistration

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object FirstRegistrationRoute: Route

fun NavController.navigateToFirstRegistration() {
    navigate(route = FirstRegistrationRoute)
}

fun NavGraphBuilder.firstRegistrationScreen(
    onBackClick: () -> Unit,
    onNextClick: (String, String) -> Unit
) {
    composable<FirstRegistrationRoute> {
        FirstRegistrationDestination(
            onBackClick = onBackClick,
            onNextClick = onNextClick
        )
    }
}
