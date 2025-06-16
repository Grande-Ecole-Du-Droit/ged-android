package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.presentation.theme.white
import com.upsaclay.common.utils.Phones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopBar(title: String) {
    TopAppBar(
        title = { Text(text = title) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBar(
    onBackClick: () -> Unit,
    title: String,
    icon: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.arrow_back_icon_description)
        )
    },
    leadingIcon: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                icon()
            }
        },
        actions = leadingIcon,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTopBar(
    modifier: Modifier = Modifier,
    title: String = "",
    onCancelClick: () -> Unit,
    onActionClick: () -> Unit,
    isButtonEnable: Boolean = true,
    buttonText: String
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onCancelClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null
                )
            }
        },
        actions = {
            Button(
                modifier = Modifier.padding(end = MaterialTheme.spacing.small),
                enabled = isButtonEnable,
                contentPadding = PaddingValues(
                    vertical = MaterialTheme.spacing.default,
                    horizontal = MaterialTheme.spacing.smallMedium
                ),
                colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.white),
                onClick = onActionClick
            ) {
                Text(text = buttonText)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val focusRequester by remember { mutableStateOf(FocusRequester()) }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    TopAppBar(
        title = {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                query = query,
                onQueryChange = onQueryChange,
                onSearch = { focusRequester.freeFocus() },
                active = false,
                onActiveChange = {
                    if (!it) {
                        focusRequester.freeFocus()
                    }
                },
                content = {},
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_ellipsis),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.inputForeground
                    )
                },
                leadingIcon = {
                    IconButton(
                        onClick = {
                            focusRequester.freeFocus()
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = { onQueryChange("") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun TitleTopBarPreview() {
    GedoiseTheme {
        TitleTopBar(title = "Title")
    }
}

@Phones
@Composable
private fun BackTopBarPreview() {
    GedoiseTheme {
        BackTopBar(
            onBackClick = {},
            title = "Title"
        )
    }
}

@Phones
@Composable
private fun EditTopBarPreview() {
    GedoiseTheme {
        EditTopBar(
            title = "Title",
            onCancelClick = { },
            onActionClick = { },
            buttonText = "Enregister"
        )
    }
}

@Phones
@Composable
private fun SearchTopBarPreview() {
    var query by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            SearchTopBar(
                query = query,
                onQueryChange = { query = it },
                onBackClick = { }
            )
        }
    }
}
