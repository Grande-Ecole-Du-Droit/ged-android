package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.white

@Composable
fun PrimaryButton(
    modifier: Modifier = Modifier,
    text: String,
    isEnable: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = isEnable,
        onClick = onClick
    ) {
        Text(text = text)
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun PrimaryButtonPreview() {
    GedoiseTheme {
        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Primary Button",
            onClick = {}
        )
    }
}