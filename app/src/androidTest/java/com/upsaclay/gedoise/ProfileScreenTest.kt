package com.upsaclay.gedoise

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.presentation.profile.ProfileScreen
import com.upsaclay.gedoise.presentation.profile.ProfileViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProfileScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val uiState = ProfileViewModel.ProfileUiState(
        user = userFixture,
        loading = false
    )
    private val profileViewModel: ProfileViewModel = mockk()

    @Before
    fun setUp() {
        every { profileViewModel.uiState } returns MutableStateFlow(uiState)
        every { profileViewModel.logout() } returns Unit
    }

    @Test
    fun logout_dialog_should_be_shown_when_logout_button_is_clicked() {
        // When
        rule.setContent {
            ProfileScreen(
                user = uiState.user,
                bottomBar = {},
                onLogoutClick = profileViewModel::logout,
                onAccountClick = { },
                onBackClick = { }
            )
        }

        rule.onNodeWithTag(rule.activity.getString(R.string.profile_screen_logout_button_tag)).performClick()


        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.profile_screen_logout_button_tag)).assertExists()
    }
}