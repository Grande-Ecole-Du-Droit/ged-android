package com.upsaclay.gedoise.presentation.navigation

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import com.upsaclay.authentication.AuthenticationBaseRoute
import com.upsaclay.authentication.AuthenticationRoute
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.entity.Route
import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.message.domain.usecase.GetUnreadConversationsCountUseCase
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.message.presentation.conversation.ConversationRoute
import com.upsaclay.news.presentation.NewsBaseRoute
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val getUnreadConversationsCountUseCase: GetUnreadConversationsCountUseCase,
    private val routeRepository: RouteRepository,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(NavigationState())
    val uiState: StateFlow<NavigationState> = _uiState
    private val _routesToNavigate = MutableSharedFlow<List<Route>>(replay = 1)
    val routesToNavigate: SharedFlow<List<Route>> = _routesToNavigate

    init {
        updateStartDestination()
        updateMessageBadges()
    }

    fun intentToNavigate(route: Route) {
        if (authenticationRepository.isAuthenticated) {
            navigate(route)
        }
    }

    private fun navigate(route: Route) {
        val routes = when(route) {
            is ChatRoute -> listOf(ConversationRoute, route)
            AuthenticationRoute -> listOf(route)
            else -> return
        }

        viewModelScope.launch {
            _routesToNavigate.emit(routes)
        }
    }

    fun setCurrentRoute(destination: NavDestination, arguments: Bundle?) {
        val route = resolveCurrentRoute(destination, arguments)
        viewModelScope.launch {
            routeRepository.setCurrentRoute(route)
        }
    }

    private fun resolveCurrentRoute(destination: NavDestination, arguments: Bundle?): Route? {
        val routeName = destination.route?.split('.')?.last() ?: return null
        return when {
            routeName.startsWith(ChatRoute.NAME) -> {
                arguments?.getString(ChatRoute.CONVERSATION_JSON_ARGUMENT)
                    ?.let { ChatRoute(conversationJson = it) }
            }
            else -> null
        }
    }

    private fun updateStartDestination() {
        viewModelScope.launch {
            authenticationRepository.authenticated.map {
                if (it) NewsBaseRoute else AuthenticationBaseRoute
            }.collect { route ->
                _uiState.update {
                    it.copy(startDestination = route)
                }
            }
        }
    }

    private fun updateMessageBadges() {
        viewModelScope.launch {
            getUnreadConversationsCountUseCase().collect { number ->
                _uiState.update {
                    it.copy(
                        topLevelDestinations = it.topLevelDestinations.map { destination ->
                            (destination as? TopLevelDestination.Message)?.copy(badges = number) ?: destination
                        }
                    )
                }
            }
        }
    }

    data class NavigationState(
        val topLevelDestinations: List<TopLevelDestination> = listOf(
            TopLevelDestination.Home(),
            TopLevelDestination.Message()
        ),
        val startDestination: Route = SplashRoute,
    )
}