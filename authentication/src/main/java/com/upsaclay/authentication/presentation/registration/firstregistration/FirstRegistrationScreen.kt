package com.upsaclay.authentication.presentation.registration.firstregistration

import androidx.annotation.StringRes
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.RegistrationScaffold
import com.upsaclay.common.domain.extensions.uppercaseFirstLetter
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.mediumPadding
import org.koin.androidx.compose.koinViewModel

@Composable
fun FirstRegistrationScreenRoute(
    onBackClick: () -> Unit,
    onNextClick: (String, String) -> Unit,
    viewModel: FirstRegistrationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FirstRegistrationScreen(
        firstName = uiState.firstName,
        lastName = uiState.lastName,
        onFirstNameChange = viewModel::onFirstNameChange,
        onLastNameChange = viewModel::onLastNameChange,
        firstNameError = uiState.firstNameError,
        lastNameError = uiState.lastNameError,
        onNextClick = {
            if (viewModel.validateInputs()) {
                onNextClick(
                    uiState.firstName.uppercaseFirstLetter(),
                    uiState.lastName.uppercaseFirstLetter()
                )
            }
        },
        onBackClick = onBackClick
    )
}

@Composable
private fun FirstRegistrationScreen(
    firstName: String,
    lastName: String,
    @StringRes firstNameError: Int? = null,
    @StringRes lastNameError: Int? = null,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    RegistrationScaffold(
        onBackClick = {
            focusManager.clearFocus()
            onBackClick()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .mediumPadding(paddingValues)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { focusManager.clearFocus() }
                    )
                }
        ) {
            FirstRegistrationForm(
                firstName = firstName,
                lastName = lastName,
                firstNameError = firstNameError,
                lastNameError = lastNameError,
                onFirstNameChange = onFirstNameChange,
                onLastNameChange = onLastNameChange
            )

            PrimaryButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .testTag(stringResource(R.string.registration_screen_next_button_tag)),
                text = stringResource(id = com.upsaclay.common.R.string.next),
                onClick = {
                    focusManager.clearFocus()
                    onNextClick()
                }
            )
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
private fun FirstRegistrationScreenPreview() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }

    GedoiseTheme {
        FirstRegistrationScreen(
            firstName = firstName,
            lastName = lastName,
            onFirstNameChange = { firstName = it.uppercaseFirstLetter() },
            onLastNameChange = { lastName = it.uppercaseFirstLetter() },
            firstNameError = null,
            lastNameError = null,
            onNextClick = {},
            onBackClick = {}
        )
    }
}