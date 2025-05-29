package com.upsaclay.authentication.presentation.registration.firstregistration

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.components.OutlineTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones

@Composable
fun FirstRegistrationForm(
    firstName: String,
    lastName: String,
    @StringRes firstNameError: Int?,
    @StringRes lastNameError: Int?,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        Text(
            text = stringResource(id = R.string.enter_first_last_name),
            style = MaterialTheme.typography.titleMedium
        )

        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = firstName,
            label = stringResource(com.upsaclay.common.R.string.first_name),
            onValueChange = onFirstNameChange,
            keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
            errorMessage = firstNameError
        )

        OutlineTextField(
            modifier = Modifier.fillMaxWidth(),
            value = lastName,
            label = stringResource(com.upsaclay.common.R.string.last_name),
            onValueChange = onLastNameChange,
            keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
            errorMessage = lastNameError
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
private fun PreviewFirstRegistrationForm() {
    GedoiseTheme {
        Surface {
            FirstRegistrationForm(
                firstName = "",
                lastName = "",
                firstNameError = null,
                lastNameError = null,
                onFirstNameChange = {},
                onLastNameChange = {}
            )
        }
    }
}