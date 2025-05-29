package com.upsaclay.authentication

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.authentication.presentation.authentication.AuthenticationDestination
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object AuthenticationBaseRoute: Route
@Serializable data object AuthenticationRoute: Route

fun NavGraphBuilder.authenticationSection(
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit,
    registrationDestinations: NavGraphBuilder.() -> Unit
) {
    navigation<AuthenticationBaseRoute>(startDestination = AuthenticationRoute) {
        composable<AuthenticationRoute> {
            AuthenticationDestination(
                onRegistrationClick = onRegistrationClick,
                onLoginClick = onLoginClick
            )
        }
        registrationDestinations()
    }
}