package com.upsaclay.authentication.presentation.registration.thirdregistration

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.authentication.presentation.components.OutlinePasswordTextField
import com.upsaclay.common.presentation.components.OutlineTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones

@Composable
fun ThirdRegistrationForm(
    email: String,
    password: String,
    loading: Boolean,
    @StringRes emailError: Int?,
    @StringRes passwordError: Int?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        Text(
            text = stringResource(id = R.string.enter_email_password),
            style = MaterialTheme.typography.titleMedium
        )

        OutlineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.registration_screen_email_input_tag)),
            value = email,
            enabled = !loading,
            onValueChange = onEmailChange,
            label = stringResource(com.upsaclay.common.R.string.email),
            errorMessage = emailError
        )

        OutlinePasswordTextField(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(R.string.registration_screen_password_input_tag)),
            text = password,
            isEnable = !loading,
            onValueChange = onPasswordChange,
            errorMessage = passwordError
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
private fun PreviewThirdRegistrationForm() {
    GedoiseTheme {
        Surface {
            ThirdRegistrationForm(
                email = "",
                password = "",
                loading = false,
                emailError = null,
                passwordError = null,
                onEmailChange = {},
                onPasswordChange = {}
            )
        }
    }
}