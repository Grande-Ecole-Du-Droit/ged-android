package com.upsaclay.gedoise.presentation.profile.account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.mediumPadding
import com.upsaclay.gedoise.R
import com.upsaclay.gedoise.presentation.components.AccountModelBottomSheet
import com.upsaclay.gedoise.presentation.components.AccountTopBar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AccountDestination(
    onBackClick: () -> Unit,
    viewModel: AccountViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))

                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    AccountScreen(
        user = uiState.user,
        loading = uiState.loading,
        screenState = uiState.screenState,
        profilePictureUri = uiState.profilePictureUri,
        snackbarHostState = snackbarHostState,
        onProfilePictureUriChange = viewModel::onProfilePictureUriChange,
        onScreenStateChange = viewModel::onScreenStateChange,
        onDeleteProfilePictureClick = viewModel::deleteProfilePicture,
        onSaveProfilePictureClick = viewModel::updateProfilePicture,
        onCancelUpdateProfilePictureClick = viewModel::resetValues,
        onBackClick = onBackClick
    )
}

@Composable
fun AccountScreen(
    user: User?,
    loading: Boolean = false,
    screenState: AccountScreenState,
    profilePictureUri: Uri?,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onProfilePictureUriChange: (Uri?) -> Unit,
    onScreenStateChange: (AccountScreenState) -> Unit,
    onDeleteProfilePictureClick: () -> Unit,
    onSaveProfilePictureClick: () -> Unit,
    onCancelUpdateProfilePictureClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteProfilePictureDialog by remember { mutableStateOf(false) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onProfilePictureUriChange(it)
                onScreenStateChange(AccountScreenState.EDIT)
            }
        }
    )

    if (showDeleteProfilePictureDialog) {
        SensibleActionDialog(
            modifier = Modifier
                .testTag(stringResource(id = R.string.account_screen_delete_profile_picture_dialog_tag)),
            title = stringResource(R.string.delete_profile_picture_dialog_title),
            text = stringResource(id = R.string.delete_profile_picture_dialog_text),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteProfilePictureDialog = false
                onDeleteProfilePictureClick()
            },
            onCancel = { showDeleteProfilePictureDialog = false }
        )
    }

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            AccountTopBar(
                isEdited = screenState == AccountScreenState.EDIT,
                onSaveClick = onSaveProfilePictureClick,
                onCancelClick = onCancelUpdateProfilePictureClick,
                onBackClick = onBackClick
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    modifier = Modifier.testTag(stringResource(id = R.string.account_screen_snackbar_tag))
                )
            }
        }
    ) { paddingValues ->
        if (user != null) {
            Surface {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .mediumPadding(paddingValues)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AccountImage(
                            modifier = Modifier.testTag(stringResource(id = R.string.account_screen_profile_picture_tag)),
                            isEdited = screenState == AccountScreenState.EDIT,
                            profilePictureUri = profilePictureUri,
                            profilePictureUrl = user.profilePictureUrl,
                            onClick = {
                                if (screenState == AccountScreenState.EDIT) {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                } else {
                                    showBottomSheet = true
                                }
                            }
                        )

                        AccountInfoItems(user = user)

                        if (showBottomSheet) {
                            AccountModelBottomSheet(
                                onDismiss = { showBottomSheet = false },
                                onNewProfilePictureClick = {
                                    singlePhotoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                showDeleteProfilePicture = user.profilePictureUrl != null,
                                onDeleteProfilePictureClick = {
                                    showDeleteProfilePictureDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AccountScreenPreview() {
    GedoiseTheme {
        Surface {
            AccountScreen(
                user = userFixture,
                loading = false,
                screenState = AccountScreenState.READ,
                profilePictureUri = null,
                snackbarHostState = SnackbarHostState(),
                onProfilePictureUriChange = {},
                onScreenStateChange = {},
                onDeleteProfilePictureClick = {},
                onSaveProfilePictureClick = {},
                onCancelUpdateProfilePictureClick = {},
                onBackClick = {}
            )
        }
    }
}