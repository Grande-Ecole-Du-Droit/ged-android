package com.upsaclay.gedoise.presentation.profile

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.ClickableItem
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.darkGray
import com.upsaclay.common.presentation.theme.lightGray
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.gedoise.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileDestination(
    onAccountClick: () -> Unit,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileScreen(
        user = uiState.user,
        bottomBar = bottomBar,
        onLogoutClick = viewModel::logout,
        onAccountClick = onAccountClick,
        onBackClick = onBackClick
    )
}

@Composable
fun ProfileScreen(
    user: User?,
    bottomBar: @Composable () -> Unit,
    onLogoutClick: () -> Unit,
    onAccountClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val dividerColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.darkGray
    } else {
        MaterialTheme.colorScheme.lightGray
    }

    DisposableEffect(Unit) {
        onDispose { showLogoutDialog = false }
    }

    if (showLogoutDialog) {
        SensibleActionDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.profile_screen_logout_dialog_tag)),
            title = stringResource(id = R.string.logout),
            text = stringResource(id = R.string.logout_dialog_message),
            cancelText = stringResource(id = com.upsaclay.common.R.string.cancel),
            confirmText = stringResource(id = R.string.logout),
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onCancel = { showLogoutDialog = false }
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.profile)
            )
        },
        bottomBar = bottomBar
    ) {

        if (user != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
            ) {
                Column {
                    TopSection(
                        profilePictureUrl = user.profilePictureFileName,
                        userFullName = user.fullName
                    )

                    HorizontalDivider(color = dividerColor)

                    ClickableItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(stringResource(id = R.string.profile_screen_account_info_button_tag)),
                        text = { Text(text = stringResource(id = R.string.account_informations)) },
                        icon = {
                            Icon(
                                modifier = Modifier.size(28.dp),
                                painter = painterResource(id = com.upsaclay.common.R.drawable.ic_fill_person),
                                contentDescription = stringResource(id = R.string.account_icon_description)
                            )
                        },
                        onClick = onAccountClick
                    )

                    ClickableItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(stringResource(id = R.string.profile_screen_logout_button_tag)),
                        text = {
                            Text(
                                text = stringResource(id = R.string.logout),
                                color = MaterialTheme.colorScheme.error
                            )
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = com.upsaclay.common.R.drawable.ic_logout),
                                contentDescription = stringResource(id = R.string.logout_icon_description),
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = { showLogoutDialog = true }
                    )
                }
            }
        }
    }
}

@Composable
private fun TopSection(profilePictureUrl: String?, userFullName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MaterialTheme.spacing.medium,
                end = MaterialTheme.spacing.medium,
                bottom = MaterialTheme.spacing.medium
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfilePicture(
            url = profilePictureUrl,
            scale = 0.7f
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

        Text(
            text = userFullName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfileScreenPreview() {
    GedoiseTheme {
        ProfileScreen(
            user = userFixture,
            onLogoutClick = {},
            onAccountClick = {},
            onBackClick = {},
            bottomBar = {},
        )
    }
}