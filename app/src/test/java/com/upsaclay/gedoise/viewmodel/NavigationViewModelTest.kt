package com.upsaclay.gedoise.viewmodel

import com.upsaclay.authentication.AuthenticationBaseRoute
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.gedoise.presentation.navigation.NavigationViewModel
import com.upsaclay.gedoise.presentation.navigation.TopLevelDestination
import com.upsaclay.message.domain.MessageJsonConverter
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.usecase.GetUnreadConversationsCountUseCase
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.news.presentation.NewsBaseRoute
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NavigationViewModelTest {
    private val routeRepository: RouteRepository = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val getUnreadConversationsCountUseCase: GetUnreadConversationsCountUseCase = mockk()

    private lateinit var navigationViewModel: NavigationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { authenticationRepository.authenticated } returns flowOf(true)
        every { authenticationRepository.isAuthenticated } returns true
        every { routeRepository.currentRoute } returns null
        every { routeRepository.setCurrentRoute(any()) } returns Unit

        navigationViewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository
        )
    }

    @Test
    fun startDestination_should_be_NewsRoute_when_authenticated() = runTest {
        // When
        navigationViewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository
        )

        // Then
        val result = navigationViewModel.uiState.value.startDestination

        assertEquals(NewsBaseRoute, result)
    }

    @Test
    fun startDestination_should_be_AuthenticationRoute_when_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.authenticated } returns flowOf(false)

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository
        )

        // Then
        val result = navigationViewModel.uiState.value.startDestination

        assertEquals(AuthenticationBaseRoute, result)
    }

    @Test
    fun intentToNavigate_should_navigate_to_screen_when_authenticated() = runTest {
        // Given
        every { authenticationRepository.isAuthenticated } returns true
        val route = ChatRoute(MessageJsonConverter.toConversationJson(conversationFixture))

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository
        )
        navigationViewModel.intentToNavigate(route)

        // Then
        val result = navigationViewModel.uiState.value.routesToNavigate

        assert(result.contains(route))
    }

    @Test
    fun intentToNavigate_should_not_navigate_when_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.isAuthenticated } returns false
        val route = ChatRoute(MessageJsonConverter.toConversationJson(conversationFixture))

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository
        )
        navigationViewModel.intentToNavigate(route)

        // Then
        val result = navigationViewModel.uiState.value.routesToNavigate

        assertEquals(emptyList(), result)
    }

    @Test
    fun updateMessageBadges_should_be_equals_to_unread_conversations_count() {
        // Given
        every { getUnreadConversationsCountUseCase() } returns flowOf(2)

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadConversationsCountUseCase = getUnreadConversationsCountUseCase,
            routeRepository = routeRepository,
            authenticationRepository = authenticationRepository
        )

        // Then
        val result = navigationViewModel.uiState.value.topLevelDestinations
        val topLevelDestination = result.find { it is TopLevelDestination.Message } as TopLevelDestination.Message

        assertEquals(2, topLevelDestination.badges)
    }
}