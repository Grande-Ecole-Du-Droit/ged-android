package com.upsaclay.authentication.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.presentation.theme.spacing

@Composable
fun OutlinePasswordTextField(
    modifier: Modifier = Modifier,
    text: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    @StringRes errorMessage: Int? = null,
    isEnable: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val icon: Painter
    val contentDescription: String
    if (passwordVisible) {
        icon = painterResource(com.upsaclay.common.R.drawable.ic_visibility)
        contentDescription = stringResource( R.string.show_password_icon_description)
    } else {
        icon = painterResource(com.upsaclay.common.R.drawable.ic_visibility_off)
        contentDescription = stringResource(R.string.hide_password_icon_description)
    }

    val errorText: (@Composable () -> Unit)? = errorMessage?.let {
        {
            Text(
                text = stringResource(errorMessage),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    OutlinedTextField(
        modifier = modifier,
        value = text,
        label = { Text(text = stringResource(id = R.string.password)) },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
        trailingIcon = {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                tint = MaterialTheme.colorScheme.inputForeground
            )
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = errorMessage != null,
        supportingText = errorText,
        enabled = isEnable,
        singleLine = true
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun OutlinedPasswordPreview() {
    var password by remember { mutableStateOf("") }

    GedoiseTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(MaterialTheme.spacing.mediumLarge)
        ) {
            OutlinePasswordTextField(
                text = password,
                onValueChange = { password = it },
                keyboardActions = KeyboardActions.Default
            )
        }
    }
}