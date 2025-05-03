package com.upsaclay.news.presentation.news

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopBar(
    userProfilePictureUrl: String? = null,
    onProfilePictureClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(com.upsaclay.common.R.string.app_name),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {},
                enabled = false
            ) {
                Image(
                    painter = painterResource(id = com.upsaclay.common.R.drawable.ged_logo),
                    contentDescription = stringResource(id = com.upsaclay.common.R.string.ged_logo_description),
                    contentScale = ContentScale.Fit
                )
            }
        },
        actions = {
            IconButton(
                onClick = onProfilePictureClick,
                modifier = Modifier.clip(shape = CircleShape)
            ) {
                ProfilePicture(url = userProfilePictureUrl)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

/*
 =====================================================================
                            Preview
 =====================================================================
 */

@Phones
@Composable
private fun NewsTopBarPreview() {
    GedoiseTheme {
        Surface {
            NewsTopBar(
                userProfilePictureUrl = null,
                onProfilePictureClick = {}
            )
        }
    }
}