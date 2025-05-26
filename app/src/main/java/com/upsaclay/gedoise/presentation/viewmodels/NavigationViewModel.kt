package com.upsaclay.gedoise.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.AuthenticationBaseRoute
import com.upsaclay.authentication.AuthenticationRoute
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.gedoise.domain.repository.ScreenRepository
import com.upsaclay.gedoise.presentation.navigation.SplashScreenRoute
import com.upsaclay.gedoise.presentation.navigation.TopLevelDestination
import com.upsaclay.message.domain.usecase.GetUnreadMessagesUseCase
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.message.presentation.conversation.ConversationRoute
import com.upsaclay.news.presentation.NewsBaseRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val getUnreadMessagesUseCase: GetUnreadMessagesUseCase,
    private val screenRepository: ScreenRepository,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(NavigationState())
    val uiState: StateFlow<NavigationState> = _uiState

    init {
        updateStartDestinationScreenRoute()
        updateMessageBadges()
        updateScreenRoute()
    }

    private fun updateScreenRoute() {
        viewModelScope.launch {
            combine(
                authenticationRepository.isAuthenticated.filterNotNull(),
                _uiState
                    .distinctUntilChangedBy { it.intentScreen }
                    .mapNotNull { it.intentScreen }
            ) { isAuthenticated, intentScreen ->
                if (isAuthenticated) {
                    intentScreen
                } else {
                    AuthenticationRoute.takeIf {
                        screenRepository.currentRoute is AuthenticationRoute
                    }
                }
            }
                .filterNotNull()
                .collect {
                    navigate(it)
                }
        }
    }

    fun intentToNavigate(route: Any) {
        _uiState.update {
            it.copy(intentScreen = route)
        }
    }

    private fun updateStartDestinationScreenRoute() {
        viewModelScope.launch {
            authenticationRepository.isAuthenticated.map {
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
            getUnreadMessagesUseCase().collect { messages ->
                _uiState.update {
                    it.copy(
                        topLevelDestinations = it.topLevelDestinations.map { destination ->
                            if (destination is TopLevelDestination.Message) {
                                destination.copy(badges = messages.size)
                            } else {
                                destination
                            }
                        }
                    )
                }
            }
        }
    }

    private fun navigate(route: Any) {
        val routes = when(route) {
            is ChatRoute -> arrayOf(ConversationRoute, route)
            AuthenticationRoute -> arrayOf(route)
            else -> return
        }

        routes.forEach { screen ->
            _uiState.update {
                it.copy(intentScreen = screen)
            }
        }
    }

    data class NavigationState(
        val topLevelDestinations: List<TopLevelDestination> = listOf(
            TopLevelDestination.Home(),
            TopLevelDestination.Message(),
        ),
        val startDestination: Any = SplashScreenRoute,
        val intentScreen: Any? = null
    )
}