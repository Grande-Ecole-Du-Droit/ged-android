package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropDownMenu(
    modifier: Modifier = Modifier,
    items: List<String>,
    selectedItem: String,
    onItemClicked: (String) -> Unit,
    expanded: Boolean,
    isEnable: Boolean = true,
    onExpandedChange: (Boolean) -> Unit,
    onDismissRequest: () -> Unit
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChange
    ) {
        OutlinedTextField(
            modifier = modifier.menuAnchor(),
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            enabled = isEnable,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(
            modifier = modifier,
            expanded = expanded,
            onDismissRequest = onDismissRequest,
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item) },
                    onClick = { onItemClicked(item) },
                    modifier = modifier
                )
            }
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
private fun SimpleDropDownMenuPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3")
    var selectedItem by remember {
        mutableStateOf(items[0])
    }
    var expanded by remember {
        mutableStateOf(false)
    }

    GedoiseTheme {
        Surface {
            SimpleDropDownMenu(
                modifier = Modifier.padding(MaterialTheme.spacing.extraSmall),
                items = items,
                selectedItem = selectedItem,
                onItemClicked = { item ->
                    selectedItem = item
                },
                expanded = expanded,
                onExpandedChange = { isExpanded ->
                    expanded = isExpanded
                },
                onDismissRequest = {
                    expanded = false
                }
            )
        }
    }
}