package com.upsaclay.authentication.presentation.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.LoginButton
import com.upsaclay.authentication.presentation.components.OutlinePasswordTextField
import com.upsaclay.common.presentation.components.OutlineTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones

@Composable
fun AuthenticationForm(
    email: String,
    password: String,
    loading: Boolean,
    emailError: Int?,
    passwordError: Int?,
    errorMessage: Int?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegistrationClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val passwordFocusRequester = remember { FocusRequester() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        CredentialsInputs(
            email = email,
            password = password,
            emailError = emailError,
            passwordError = passwordError,
            passwordFocusRequester = passwordFocusRequester,
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange
        )

        errorMessage?.let {
            passwordFocusRequester.requestFocus()
            Text(
                modifier = Modifier.align(Alignment.Start),
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        LoginButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.authentication_screen_login_button_tag)),
            text = stringResource(id = R.string.login),
            isLoading = loading,
            onClick = {
                focusManager.clearFocus()
                onLoginClick()
            }
        )

        RegistrationText(
            onRegistrationClick = {
                focusManager.clearFocus()
                onRegistrationClick()
            }
        )
    }
}


@Composable
private fun CredentialsInputs(
    email: String,
    password: String,
    emailError: Int?,
    passwordError: Int?,
    passwordFocusRequester: FocusRequester,
    keyboardActions: KeyboardActions,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            label = stringResource(com.upsaclay.common.R.string.email),
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            errorMessage = emailError
        )

        OutlinePasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            text = password,
            onValueChange = onPasswordChange,
            keyboardActions = keyboardActions,
            errorMessage = passwordError
        )
    }
}

@Composable
private fun RegistrationText(
    onRegistrationClick: () -> Unit
) {
    Row {
        Text(
            text = stringResource(id = R.string.not_register_yet),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))

        Text(
            text = AnnotatedString(stringResource(id = R.string.sign_up)),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .clickable { onRegistrationClick() }
                .testTag(stringResource(id = R.string.authentication_screen_registration_button_tag))
        )
    }
}


/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AuthenticationFormPreview() {
    GedoiseTheme {
        Surface {
            AuthenticationForm(
                email = "",
                password = "",
                loading = false,
                emailError = null,
                passwordError = null,
                errorMessage = null,
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onRegistrationClick = {}
            )
        }
    }
}