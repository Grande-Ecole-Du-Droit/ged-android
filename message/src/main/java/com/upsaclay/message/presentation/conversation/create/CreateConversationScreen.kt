package com.upsaclay.message.presentation.conversation.create

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.upsaclay.common.presentation.components.SearchTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateConversationDestination(
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
        onResetQuery = viewModel::resetQuery,
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
    onResetQuery: () -> Unit,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var search by remember { mutableStateOf(false) }

    BackHandler(enabled = search) {
        search = false
    }

    Scaffold(
        topBar = {
            if (!search) {
                BackTopBar(
                    onBackClick = {
                        keyboardController?.hide()
                        onBackClick()
                    },
                    title = stringResource(id = R.string.new_conversation),
                    leadingIcon = {
                        IconButton(
                            onClick = { search = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    }
                )
            } else {
                SearchTopBar(
                    query = query,
                    onQueryChange = onQueryChange,
                    onBackClick = {
                        search = false
                        onResetQuery()
                    }
                )
            }
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
            if (loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressBar()
                }
            } else {
                UsersFeed(
                    users = users,
                    onUserClick = {
                        keyboardController?.hide()
                        onUserClick(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun UsersFeed(
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    LazyColumn {
        if (users.isNotEmpty()) {
            items(users) { user ->
                UserItem(
                    user = user,
                    onClick = onUserClick
                )
            }
        } else {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.spacing.medium),
                    text = stringResource(id = com.upsaclay.common.R.string.users_not_found),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.previewText
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

@Preview(showBackground = true)
@Composable
private fun CreateConversationScreenPreview() {
    val users: List<User> = usersFixture + usersFixture
    var loading by remember { mutableStateOf(true) }
    var query by remember { mutableStateOf("") }

    GedoiseTheme {
        CreateConversationScreen(
            users = users,
            query = query,
            loading = loading,
            onQueryChange = { query = it },
            onResetQuery = { query = "" },
            onUserClick = {},
            onBackClick = {}
        )
    }
}