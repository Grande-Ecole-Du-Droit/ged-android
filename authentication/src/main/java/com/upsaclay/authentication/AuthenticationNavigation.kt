package com.upsaclay.authentication

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.upsaclay.authentication.presentation.authentication.AuthenticationScreenRoute
import com.upsaclay.common.domain.entity.Route
import kotlinx.serialization.Serializable

@Serializable data object AuthenticationBaseRoute: Route
@Serializable data object AuthenticationRoute: Route

fun NavGraphBuilder.authenticationSection(
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit,
    registrationDestination: NavGraphBuilder.() -> Unit
) {
    navigation<AuthenticationBaseRoute>(startDestination = AuthenticationRoute) {
        composable<AuthenticationRoute> {
            AuthenticationScreenRoute(
                onRegistrationClick = onRegistrationClick,
                onLoginClick = onLoginClick
            )
        }
        registrationDestination()
    }
}