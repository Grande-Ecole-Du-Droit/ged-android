package com.upsaclay.authentication.presentation.registration.secondregistration

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data class SecondRegistrationRoute(val firstName: String, val lastName: String): Route

fun NavController.navigateToSecondRegistration(firstName: String, lastName: String) {
    navigate(route = SecondRegistrationRoute(firstName, lastName))
}

fun NavGraphBuilder.secondRegistrationScreen(
    onBackClick: () -> Unit,
    onNextClick: (String, String, String) -> Unit
) {
    composable<SecondRegistrationRoute> { entry ->
        val firstName = entry.toRoute<SecondRegistrationRoute>().firstName
        val lastName = entry.toRoute<SecondRegistrationRoute>().lastName

        SecondRegistrationScreenRoute(
            onNextClick = { schoolLevel ->
                onNextClick(firstName, lastName, schoolLevel)
            },
            onBackClick = onBackClick
        )
    }
}