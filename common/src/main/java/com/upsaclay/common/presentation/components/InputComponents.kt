package com.upsaclay.common.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import kotlinx.coroutines.android.awaitFrame

@Composable
fun OutlineTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    @StringRes errorMessage: Int? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false
) {
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
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        isError = errorMessage != null,
        keyboardActions = keyboardActions,
        singleLine = true,
        supportingText = errorText,
        enabled = enabled,
        readOnly = readOnly
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    textStyle: TextStyle = TextStyle.Default,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    padding: Dp = MaterialTheme.spacing.default,
    enabled: Boolean = true
) {
    val colors: TextFieldColors = TextFieldDefaults.colors()

    BasicTextField(
        modifier = modifier
            .background(backgroundColor)
            .padding(padding),
        enabled = enabled,
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        cursorBrush = SolidColor(colors.cursorColor)
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = placeholder,
            contentPadding = PaddingValues(MaterialTheme.spacing.default)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransparentFocusedTextField(
    modifier: Modifier = Modifier,
    value: String = "",
    onValueChange: (String) -> Unit,
    placeholder: @Composable (() -> Unit),
    textStyle: TextStyle = TextStyle.Default,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    padding: PaddingValues = PaddingValues(MaterialTheme.spacing.default),
    shape: Shape = TextFieldDefaults.shape,
    displayKeyboard: Boolean = true,
    enabled: Boolean = true
) {
    val focusRequester = remember { FocusRequester() }
    val colors: TextFieldColors = TextFieldDefaults.colors()

    if (displayKeyboard) {
        LaunchedEffect(Unit) {
            awaitFrame()
            focusRequester.requestFocus()
        }
    }

    BasicTextField(
        modifier =  modifier
            .then(if (displayKeyboard) Modifier.focusRequester(focusRequester) else Modifier)
            .clip(shape)
            .background(backgroundColor)
            .padding(padding),
        enabled = enabled,
        value = value,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        cursorBrush = SolidColor(colors.cursorColor)
    ) { innerTextField ->
        val interactionSource = remember { MutableInteractionSource() }
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = false,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = placeholder,
            contentPadding = PaddingValues(MaterialTheme.spacing.default)
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
private fun OutlinedTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            OutlineTextField(
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                value = text,
                label = "Label",
                onValueChange = { text = it }
            )
        }
    }
}

@Preview
@Composable
private fun TransparentTextFieldPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Placeholder") },
            backgroundColor = MaterialTheme.colorScheme.background,
            padding = MaterialTheme.spacing.medium,
        )
    }
}