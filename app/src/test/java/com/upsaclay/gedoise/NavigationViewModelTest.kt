package com.upsaclay.gedoise

import com.upsaclay.authentication.AuthenticationBaseRoute
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.presentation.registration.first.FirstRegistrationRoute
import com.upsaclay.gedoise.domain.repository.ScreenRepository
import com.upsaclay.gedoise.presentation.navigation.TopLevelDestination
import com.upsaclay.gedoise.presentation.viewmodels.NavigationViewModel
import com.upsaclay.message.domain.MessageJsonConverter
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.messageFixture
import com.upsaclay.message.domain.messagesFixture
import com.upsaclay.message.domain.usecase.GetUnreadMessagesUseCase
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
    private val screenRepository: ScreenRepository = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val getUnreadMessagesUseCase: GetUnreadMessagesUseCase = mockk()

    private lateinit var navigationViewModel: NavigationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { getUnreadMessagesUseCase() } returns flowOf(messagesFixture)
        every { authenticationRepository.isAuthenticated } returns flowOf(true)
        every { screenRepository.currentRoute } returns null
        every { screenRepository.setCurrentScreenRoute(any()) } returns Unit

        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )
    }

    @Test
    fun startDestinationScreenRoute_should_be_news_screen_when_authenticated() = runTest {
        // When
        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )

        // Then
        val result = navigationViewModel.uiState.value.startDestination

        assertEquals(NewsBaseRoute, result)
    }

    @Test
    fun startDestinationScreenRoute_should_be_authentication_screen_when_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.isAuthenticated } returns flowOf(false)

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )

        // Then
        val result = navigationViewModel.uiState.value.startDestination

        assertEquals(AuthenticationBaseRoute, result)
    }

    @Test
    fun intentToNavigate_should_update_intentScreen() = runTest {
        // Given
        val screen = ChatRoute(MessageJsonConverter.fromConversation(conversationFixture))

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )
        navigationViewModel.intentToNavigate(screen)

        // Then
        val result = navigationViewModel.uiState.value.intentScreen

        assertEquals(screen, result)
    }

    @Test
    fun should_navigate_to_screen_when_authenticated() = runTest {
        // Given
        every { authenticationRepository.isAuthenticated } returns flowOf(true)
        val screen = ChatRoute(MessageJsonConverter.fromConversation(conversationFixture))

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )
        navigationViewModel.intentToNavigate(screen)

        // Then
        val result = navigationViewModel.uiState.value.intentScreen

        assertEquals(screen, result)
    }

    @Test
    fun should_navigate_to_authentication_screen_when_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.isAuthenticated } returns flowOf(false)
        val screen = ChatRoute(MessageJsonConverter.fromConversation(conversationFixture))

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )
        navigationViewModel.intentToNavigate(screen)

        // Then
        val result = navigationViewModel.uiState.value.startDestination

        assertEquals(AuthenticationBaseRoute, result)
    }

    @Test
    fun should_not_navigate_when_current_screen_is_one_of_authentication_screen_when_unauthenticated() = runTest {
        // Given
        val screen = FirstRegistrationRoute
        every { authenticationRepository.isAuthenticated } returns flowOf(false)
        every { screenRepository.currentRoute } returns FirstRegistrationRoute

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )
        navigationViewModel.intentToNavigate(screen)

        // Then
        val result = navigationViewModel.uiState.value.intentScreen

        assertEquals(result, navigationViewModel.uiState.value.intentScreen)
    }

    @Test
    fun updateMessageBadges_should_be_equals_to_unread_messages() {
        // Given
        val unreadMessage = 2
        every { getUnreadMessagesUseCase() } returns flowOf(listOf(messageFixture, messageFixture))

        // When
        navigationViewModel = NavigationViewModel(
            getUnreadMessagesUseCase = getUnreadMessagesUseCase,
            screenRepository = screenRepository,
            authenticationRepository = authenticationRepository
        )

        // Then
        val result = navigationViewModel.uiState.value.topLevelDestinations
        val topLevelDestination = result.find { it is TopLevelDestination.Message } as TopLevelDestination.Message

        assertEquals(unreadMessage, topLevelDestination.badges)
    }
}