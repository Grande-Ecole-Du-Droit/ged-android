package com.upsaclay.common.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputBackground
import com.upsaclay.common.presentation.theme.cursor
import com.upsaclay.common.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    modifier: Modifier = Modifier,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle.Default.copy(color = MaterialTheme.colorScheme.onBackground),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.cursor)
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            innerTextField = innerTextField,
            value = value,
            placeholder = { Text(text = placeholder) },
            shape = ShapeDefaults.ExtraLarge,
            enabled = true,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                cursorColor = MaterialTheme.colorScheme.cursor
            ),
            contentPadding = PaddingValues(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() }
        )
    }
}

/**
 * =====================================================================
 *                                 Preview
 * =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun SearchBarPreview() {
    GedoiseTheme {
        Box(
            modifier = Modifier.padding(MaterialTheme.spacing.small)
        ) {
            SimpleSearchBar(
                modifier = Modifier.fillMaxWidth(),
                placeholder = "Search",
                value = "",
                onValueChange = {}
            )
        }
    }
}