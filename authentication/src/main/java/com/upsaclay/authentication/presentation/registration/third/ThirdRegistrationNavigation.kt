package com.upsaclay.authentication.presentation.registration.third

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
data class ThirdRegistrationRoute(val firstName: String, val lastName: String, val schoolLevel: String)


fun NavController.navigateToThirdRegistration(firstName: String, lastName: String, schoolLevel: String) {
    navigate(route = ThirdRegistrationRoute(firstName, lastName, schoolLevel))
}

fun NavGraphBuilder.thirdRegistrationScreen(
    onBackClick: () -> Unit,
    onRegistrationClick: () -> Unit
) {
    composable<ThirdRegistrationRoute> { entry ->
        val firstName = entry.toRoute<ThirdRegistrationRoute>().firstName
        val lastName = entry.toRoute<ThirdRegistrationRoute>().lastName
        val schoolLevel = entry.toRoute<ThirdRegistrationRoute>().schoolLevel

        ThirdRegistrationScreen(
            firstName = firstName,
            lastName = lastName,
            schoolLevel = schoolLevel,
            onBackClick = onBackClick,
            onRegistrationClick = onRegistrationClick
        )
    }
}