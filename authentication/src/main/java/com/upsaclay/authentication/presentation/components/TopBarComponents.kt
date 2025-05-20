package com.upsaclay.authentication.presentation.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.authentication.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun RegistrationScaffold(
    onBackClick: () -> Unit,
    snackbarHostState: SnackbarHostState? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.registration),
                        modifier = Modifier.fillMaxWidth(),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = com.upsaclay.common.R.drawable.ic_partial_arrow_left),
                            contentDescription = stringResource(
                                id = com.upsaclay.common.R.string.arrow_back_icon_description
                            )
                        )
                    }
                }
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
        RegistrationScaffold(onBackClick = {}) {
            Text(
                text = stringResource(id = R.string.registration),
                modifier = Modifier.fillMaxSize(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}