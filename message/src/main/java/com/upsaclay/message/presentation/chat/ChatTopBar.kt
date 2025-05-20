package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopBar(
    navController: NavController,
    interlocutor: User,
    onClickBack: () -> Unit
) {
    val color = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)

    Row(
        modifier = Modifier
            .background(color = color.containerColor)
            .padding(
                horizontal = MaterialTheme.spacing.extraSmall,
                vertical = MaterialTheme.spacing.small
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onClickBack()
            navController.popBackStack()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = com.upsaclay.common.R.string.arrow_back_icon_description)
            )
        }

        ProfilePicture(url = interlocutor.profilePictureFileName, scale = 0.4f)

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.smallMedium))

        Text(text = interlocutor.fullName, style = MaterialTheme.typography.titleMedium)
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview
@Composable
private fun ChatTopBarPreview() {
    GedoiseTheme {
        Column {
            ChatTopBar(
                navController = rememberNavController(),
                interlocutor = userFixture,
                onClickBack = { }
            )
        }
    }
}