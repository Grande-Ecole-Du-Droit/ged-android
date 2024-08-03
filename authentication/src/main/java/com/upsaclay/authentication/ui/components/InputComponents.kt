package com.upsaclay.authentication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.upsaclay.common.R
import com.upsaclay.common.ui.theme.GedoiseColor
import com.upsaclay.common.ui.theme.GedoiseTheme

@Composable
fun OutlinedEmailInput(
    modifier: Modifier = Modifier,
    text: String,
    label: String = stringResource(id = R.string.email),
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    isEnable: Boolean = true,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        isError = isError,
        keyboardActions = keyboardActions,
        singleLine = true,
        enabled = isEnable,
        readOnly = readOnly,
    )
}

@Composable
fun OutlinedPasswordInput(
    modifier: Modifier = Modifier,
    text: String,
    label: String = stringResource(id = com.upsaclay.authentication.R.string.password),
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    isEnable: Boolean = true
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val icon: Painter
    val contentDescription: String
    if (!passwordVisible) {
        icon = painterResource(id = R.drawable.ic_visibility)
        contentDescription = stringResource(
            id = com.upsaclay.authentication.R.string.show_password_icon_description
        )
    } else {
        icon = painterResource(id = R.drawable.ic_visibility_off)
        contentDescription = stringResource(
            id = com.upsaclay.authentication.R.string.hide_password_icon_description
        )
    }
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = text,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        keyboardActions = keyboardActions,
        trailingIcon = {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                tint = GedoiseColor.BlackIconColor
            )
        },
        visualTransformation =
        if (passwordVisible) VisualTransformation.None
        else PasswordVisualTransformation(),
        isError = isError,
        enabled = isEnable,
        singleLine = true
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 220)
@Composable
fun OutlinedInputsPreview() {
    var mail by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    GedoiseTheme {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        ) {
            OutlinedEmailInput(
                text = mail,
                onValueChange = { mail = it }
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedPasswordInput(
                text = password,
                onValueChange = { password = it }
            )
        }
    }
}