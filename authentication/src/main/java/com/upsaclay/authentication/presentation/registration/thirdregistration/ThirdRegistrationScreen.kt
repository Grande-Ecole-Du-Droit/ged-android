package com.upsaclay.authentication.presentation.registration.thirdregistration

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.RegistrationScaffold
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.presentation.components.LinearProgressBar
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.mediumPadding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ThirdRegistrationScreenRoute(
    firstName: String,
    lastName: String,
    schoolLevel: String,
    onBackClick: () -> Unit,
    onRegistrationClick: () -> Unit,
    viewModel: ThirdRegistrationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
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

                is SingleUiEvent.Success -> onRegistrationClick()
            }
        }
    }

    ThirdRegistrationScreen(
        email = uiState.email,
        password = uiState.password,
        loading = uiState.loading,
        emailError = uiState.emailError,
        passwordError = uiState.passwordError,
        snackbarHostState = snackbarHostState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRegistrationClick = { viewModel.register(firstName, lastName, schoolLevel) },
        onBackClick = onBackClick
    )
}

@Composable
private fun ThirdRegistrationScreen(
    email: String,
    password: String,
    loading: Boolean,
    @StringRes emailError: Int? = null,
    @StringRes passwordError: Int? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegistrationClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    RegistrationScaffold(
        onBackClick = onBackClick,
        snackbarHostState = snackbarHostState
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.smallMedium)
        ) {
            if (loading) {
                LinearProgressBar(modifier = Modifier.fillMaxWidth())
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .mediumPadding()
                    .pointerInput(Unit) {
                        detectTapGestures(onPress = { focusManager.clearFocus() })
                    }
            ) {
                ThirdRegistrationForm(
                    email = email,
                    password = password,
                    loading = loading,
                    emailError = emailError,
                    passwordError = passwordError,
                    onEmailChange = onEmailChange,
                    onPasswordChange = onPasswordChange
                )

                PrimaryButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .testTag(stringResource(R.string.registration_screen_next_button_tag)),
                    isEnable = !loading,
                    text = stringResource(id = com.upsaclay.common.R.string.next),
                    onClick = onRegistrationClick
                )
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
private fun ThirdRegistrationScreenPreview() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by remember { mutableStateOf(false) }

    GedoiseTheme {
        Surface {
            ThirdRegistrationScreen(
                email = email,
                password = password,
                loading = isLoading,
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onRegistrationClick = {},
                onBackClick = {},
            )
        }
    }
}