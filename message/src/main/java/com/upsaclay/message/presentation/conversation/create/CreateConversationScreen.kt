package com.upsaclay.message.presentation.conversation.create

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.SimpleSearchBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateConversationScreenRoute(
    onBackClick: () -> Unit,
    onCreateConversationClick: (Conversation) -> Unit,
    viewModel: CreateConversationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            if (it is SingleUiEvent.Error) {
                showSnackBar(context.getString(it.messageId))
            }
        }
    }

    CreateConversationScreen(
        users = uiState.users,
        query = uiState.query,
        loading = uiState.loading,
        snackbarHostState = snackbarHostState,
        onQueryChange = viewModel::onQueryChange,
        onUserClick = { user ->
            scope.launch {
                viewModel.getConversation(user)?.let(onCreateConversationClick)
            }
        },
        onBackClick = onBackClick
    )
}

@Composable
fun CreateConversationScreen(
    users: List<User>,
    query: String,
    loading: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onQueryChange: (String) -> Unit,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = {
                    keyboardController?.hide()
                    onBackClick()
                },
                title = stringResource(id = R.string.new_conversation)
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            SimpleSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium),
                placeholder = stringResource(id = com.upsaclay.common.R.string.search),
                value = query,
                onValueChange = onQueryChange
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (loading) {
                    CircularProgressBar(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = MaterialTheme.spacing.large),
                        scale = 0.5f
                    )
                } else {
                    LazyColumn {
                        if (users.isNotEmpty()) {
                            items(users) { user ->
                                UserItem(
                                    user = user,
                                    onClick = {
                                        keyboardController?.hide()
                                        onUserClick(it)
                                    }
                                )
                            }
                        } else {
                            item {
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = com.upsaclay.common.R.string.user_not_found),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.previewText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(showBackground = true)
@Composable
private fun CreateConversationScreenPreview() {
    val users: List<User> = usersFixture + usersFixture
    var loading by remember { mutableStateOf(true) }

    GedoiseTheme {
        Scaffold(
            topBar = {
                BackTopBar(
                    onBackClick = { },
                    title = stringResource(id = R.string.new_conversation)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                SimpleSearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.medium),
                    placeholder = stringResource(id = com.upsaclay.common.R.string.search),
                    value = "",
                    onValueChange = { }
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    if (loading) {
                        CircularProgressBar(
                            modifier = Modifier.align(Alignment.Center),
                            scale = 1.5f
                        )
                    }

                    LazyColumn {
                        if (users.isNotEmpty()) {
                            items(users) { user ->
                                UserItem(
                                    user = user,
                                    onClick = { }
                                )
                            }
                        } else {
                            item {
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = stringResource(id = com.upsaclay.common.R.string.user_not_found),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.previewText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}