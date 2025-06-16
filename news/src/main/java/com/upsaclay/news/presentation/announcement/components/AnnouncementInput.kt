package com.upsaclay.news.presentation.announcement.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.common.presentation.components.TransparentFocusedTextField
import com.upsaclay.common.presentation.components.TransparentTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.hintText
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R

@Composable
fun AnnouncementInput(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        AnnouncementTitleInput(title = title, onTitleChange = onTitleChange)
        AnnouncementContentInput(content = content, onContentChange = onContentChange)
    }
}

@Composable
private fun AnnouncementTitleInput(
    title: String,
    onTitleChange: (String) -> Unit
) {
    val textStyle = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.3f
    )
    TransparentFocusedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = title,
        placeholder = {
            Text(
                text = stringResource(id = R.string.title_field_entry),
                style = textStyle,
                color = MaterialTheme.colorScheme.hintText
            )
        },
        onValueChange = {
            val truncated = if (it.length <= 300) it else it.take(300)
            onTitleChange(truncated)
        },
        textStyle = textStyle
    )
}

@Composable
private fun AnnouncementContentInput(
    content: String,
    onContentChange: (String) -> Unit
) {
    val textStyle = MaterialTheme.typography.bodyLarge
    TransparentTextField(
        modifier = Modifier.fillMaxWidth(),
        value = content,
        placeholder = {
            Text(
                text = stringResource(id = R.string.content_field_entry),
                style = textStyle,
                color = MaterialTheme.colorScheme.hintText
            )
        },
        onValueChange = {
            val truncated = if (it.length <= 2000) it else it.take(2000)
            onContentChange(truncated)
        },
        textStyle = textStyle
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AnnouncementInputPreview() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            AnnouncementInput(
                modifier = Modifier.padding(MaterialTheme.spacing.medium),
                title = title,
                content = content,
                onTitleChange = { title = it },
                onContentChange = { content = it }
            )
        }
    }
}