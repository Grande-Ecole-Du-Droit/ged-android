package com.upsaclay.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.upsaclay.common.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones

@Composable
fun SimpleDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    confirmText: String = stringResource(id = R.string.accept),
    cancelText: String = stringResource(id = R.string.cancel),
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit = onCancel
) {
    AlertDialog(
        modifier = modifier,
        title = { title?.let { Text(text = title) } },
        text = { text?.let { Text(text = text) } },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = cancelText)
            }
        }
    )
}

@Composable
fun SensibleActionDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String? = null,
    confirmText: String,
    cancelText: String = stringResource(id = R.string.cancel),
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit = onCancel
) {
    AlertDialog(
        modifier = modifier,
        title = { title?.let { Text(text = title) } },
        text = { text?.let { Text(text = text) } },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = cancelText)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog(message: String = stringResource(id = R.string.loading)) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    BasicAlertDialog(
        onDismissRequest = { },
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.extraSmall
            )
            .widthIn(max = screenWidth * 0.4f)
            .padding(vertical = MaterialTheme.spacing.mediumLarge)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressBar(scale = 0.5f)

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.medium))

            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
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
private fun SimpleDialogPreview() {
    GedoiseTheme {
        SimpleDialog(
            title = "Simple dialog",
            text = "There is the text area",
            confirmText = "Confirm",
            cancelText = "Cancel",
            onConfirm = {},
            onCancel = {},
        )
    }
}

@Phones
@Composable
private fun SensibleActionDialogPreview() {
    GedoiseTheme {
        SensibleActionDialog(
            title = "Sensible action",
            text = "Do you want to do this sensible action ?",
            confirmText = "Delete",
            cancelText = "Cancel",
            onConfirm = {},
            onCancel = {},
            onDismiss = {}
        )
    }
}

@Phones
@Composable
private fun LoadingDialogPreview() {
    GedoiseTheme {
        LoadingDialog(
            message = "Waiting..."
        )
    }
}