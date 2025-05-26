package com.upsaclay.authentication.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@Composable
internal fun RegistrationScaffold(
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(id = R.string.registration)
            )
        },
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(it) { data ->
                    Snackbar(data)
                }
            }
        }
    ) { paddingsValues ->
        Surface {
            content(paddingsValues)
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
private fun RegistrationScaffoldPreview() {
    GedoiseTheme {
        RegistrationScaffold(onBackClick = {}) {}
    }
}