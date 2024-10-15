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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing

@Composable
fun SimpleDialog(
    title: String,
    message: String,
    confirmText: String = stringResource(id = R.string.accept),
    cancelText: String = stringResource(id = R.string.cancel),
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text(text = title) },
        text = { Text(text = message) },
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
    message: String,
    confirmText: String,
    cancelText: String = stringResource(id = R.string.cancel),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        text = {
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
        },
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
fun LoadingDialog(text: String = stringResource(id = R.string.loading)) {
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
                text = text,
                style = MaterialTheme.typography.bodySmall,
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

@Preview
@Composable
private fun SimpleDialogPreview() {
    GedoiseTheme {
        SimpleDialog(
            title = "Simple dialog",
            message = "There is the text area",
            confirmText = "Confirm",
            cancelText = "Cancel",
            onConfirm = { },
            onCancel = { },
            onDismiss = { }
        )
    }
}

@Preview
@Composable
private fun SensibleActionDialogPreview() {
    GedoiseTheme {
        SensibleActionDialog(
            message = "Do you want delete this item ?",
            confirmText = "Delete",
            cancelText = "Cancel",
            onConfirm = { },
            onCancel = { },
            onDismiss = { }
        )
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
private fun InformationDialogPreview() {
    GedoiseTheme {
        LoadingDialog(
            text = "Waiting..."
        )
    }
}