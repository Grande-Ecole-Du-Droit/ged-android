package com.upsaclay.authentication.presentation.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegistrationClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        CredentialsInputs(
            email = email,
            password = password,
            emailError = emailError,
            passwordError = passwordError,
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            onEmailChange = onEmailChange,
            onPasswordChange = onPasswordChange
        )

        LoginButton(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.authentication_screen_login_button_tag)),
            text = stringResource(id = R.string.login),
            isLoading = loading,
            onClick = {
                keyboardController?.hide()
                onLoginClick()
            }
        )

        RegistrationText(
            onRegistrationClick = {
                keyboardController?.hide()
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
    keyboardActions: KeyboardActions,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column {
        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = onEmailChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            errorMessage = emailError,
            label = stringResource(com.upsaclay.common.R.string.email)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        OutlinePasswordTextField(
            modifier = Modifier.fillMaxWidth(),
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
                onEmailChange = {},
                onPasswordChange = {},
                onLoginClick = {},
                onRegistrationClick = {}
            )
        }
    }
}