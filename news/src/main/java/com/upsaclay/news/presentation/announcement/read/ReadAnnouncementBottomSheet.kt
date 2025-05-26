package com.upsaclay.news.presentation.announcement.read

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.ClickableItem
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.news.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadAnnouncementBottomSheet(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val hideBottomSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss()
            }
        }
    }

    ModalBottomSheet(
        modifier = Modifier.testTag(stringResource(id = R.string.read_screen_bottom_sheet_tag)),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        ClickableItem(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.read_screen_sheet_edit_field_tag)),
            text = { Text(text = stringResource(id = com.upsaclay.common.R.string.edit)) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            },
            onClick = {
                hideBottomSheet()
                onEditClick()
            }
        )

        ClickableItem(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.read_screen_sheet_delete_field_tag)),
            text = {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = {
                hideBottomSheet()
                onDeleteClick()
            }
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
    }
}