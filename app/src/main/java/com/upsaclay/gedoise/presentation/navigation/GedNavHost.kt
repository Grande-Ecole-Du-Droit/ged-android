package com.upsaclay.gedoise.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.upsaclay.authentication.authenticationSection
import com.upsaclay.authentication.presentation.registration.first.firstRegistrationScreen
import com.upsaclay.authentication.presentation.registration.first.navigateToFirstRegistration
import com.upsaclay.authentication.presentation.registration.second.navigateToSecondRegistration
import com.upsaclay.authentication.presentation.registration.second.secondRegistrationScreen
import com.upsaclay.authentication.presentation.registration.third.navigateToThirdRegistration
import com.upsaclay.authentication.presentation.registration.third.thirdRegistrationScreen
import com.upsaclay.common.presentation.TopLevelDestinationRoute
import com.upsaclay.gedoise.presentation.components.MainBottomBar
import com.upsaclay.gedoise.presentation.profile.account.accountScreen
import com.upsaclay.gedoise.presentation.profile.account.navigateToAccount
import com.upsaclay.gedoise.presentation.profile.navigateToProfile
import com.upsaclay.gedoise.presentation.profile.profileSection
import com.upsaclay.gedoise.presentation.viewmodels.NavigationViewModel
import com.upsaclay.message.presentation.chat.chatScreen
import com.upsaclay.message.presentation.chat.navigateToChat
import com.upsaclay.message.presentation.conversation.conversationSection
import com.upsaclay.message.presentation.conversation.create.CreateConversationRoute
import com.upsaclay.message.presentation.conversation.create.createConversationScreen
import com.upsaclay.message.presentation.conversation.create.navigateToCreateConversation
import com.upsaclay.message.presentation.conversation.navigateToConversation
import com.upsaclay.news.presentation.NewsRoute
import com.upsaclay.news.presentation.announcement.create.createAnnouncementScreen
import com.upsaclay.news.presentation.announcement.create.navigateToCreateAnnouncement
import com.upsaclay.news.presentation.announcement.edit.editAnnouncementScreen
import com.upsaclay.news.presentation.announcement.edit.navigateToEditAnnouncement
import com.upsaclay.news.presentation.announcement.read.navigateToReadAnnouncement
import com.upsaclay.news.presentation.announcement.read.readAnnouncementScreen
import com.upsaclay.news.presentation.navigateToNews
import com.upsaclay.news.presentation.newsSection
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable data object SplashScreenRoute

@Composable
fun GedNavHost(
    navigationViewModel: NavigationViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val uiState by navigationViewModel.uiState.collectAsState()
    val currentEntry = navController.currentBackStackEntryAsState()
    val navOptions = NavOptions.Builder()
        .setLaunchSingleTop(true)
        .setRestoreState(true)
        .setPopUpTo(
            route = NewsRoute,
            inclusive = false,
            saveState = true
        )
        .build()

    fun NavController.navigateToTopLevelDestination(destination: TopLevelDestinationRoute) {
        when (destination) {
            TopLevelDestinationRoute.HOME -> navigateToNews(navOptions = navOptions)
            TopLevelDestinationRoute.MESSAGE -> navigateToConversation(navOptions = navOptions)
        }
    }

    val bottomBar: @Composable () -> Unit = {
        MainBottomBar(
            onTopLevelDestinationClick = navController::navigateToTopLevelDestination,
            currentRoute = currentEntry.value?.destination,
            topLevelDestinations = uiState.topLevelDestinations
        )
    }

    NavHost(
        navController = navController,
        startDestination = uiState.startDestination
    ) {
        composable<SplashScreenRoute> {}

        authenticationSection(
            onRegistrationClick = navController::navigateToFirstRegistration,
            onLoginClick = navController::navigateToNews
        ) {
            firstRegistrationScreen(
                onBackClick = navController::popBackStack,
                onNextClick = navController::navigateToSecondRegistration
            )
            secondRegistrationScreen(
                onBackClick = navController::popBackStack,
                onNextClick = navController::navigateToThirdRegistration
            )
            thirdRegistrationScreen(
                onBackClick = navController::popBackStack,
                onRegistrationClick = navController::navigateToNews
            )
        }

        newsSection(
            onAnnouncementClick = navController::navigateToReadAnnouncement,
            onCreateAnnouncementClick = navController::navigateToCreateAnnouncement,
            onProfilePictureClick = navController::navigateToProfile,
            bottomBar = bottomBar
        ) {
            createAnnouncementScreen(
                onBackClick = navController::popBackStack
            )

            readAnnouncementScreen(
                onBackClick = navController::popBackStack,
                onEditClick = navController::navigateToEditAnnouncement
            )

            editAnnouncementScreen(
                onBackClick = navController::popBackStack
            )
        }

        conversationSection(
            onConversationClick = navController::navigateToChat,
            onCreateConversation = navController::navigateToCreateConversation,
            bottomBar = bottomBar
        ) {
            createConversationScreen(
                onBackClick = navController::popBackStack,
                onCreateConversationClick = {
                    navController.navigateToChat(it) {
                        popUpTo(CreateConversationRoute) {
                            inclusive = true
                        }
                    }
                }
            )

            chatScreen(
                onBackClick = navController::popBackStack
            )
        }

        profileSection(
            onAccountClick = navController::navigateToAccount,
            onBackClick = navController::popBackStack,
            bottomBar = bottomBar
        ) {
            accountScreen(onBackClick = navController::popBackStack)
        }
    }
}