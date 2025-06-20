package com.upsaclay.message.presentation.chat

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.black
import com.upsaclay.common.presentation.theme.cursor
import com.upsaclay.common.presentation.theme.inputBackground
import com.upsaclay.common.presentation.theme.inputForeground
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.presentation.theme.white
import com.upsaclay.common.utils.FormatLocalDateTimeUseCase
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.messageFixture
import java.time.LocalDateTime

@Composable
fun SentMessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    showSeen: Boolean = false,
    onClick: () -> Unit = {}
) {
    val dateTimeTextColor = if (isSystemInDarkTheme()) Color.LightGray else Color(0xFFC8C8C8)

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Spacer(modifier = Modifier.weight(0.2f))

        Column(
            modifier = Modifier.weight(0.8f, fill = false),
            horizontalAlignment = Alignment.End
        ) {
            MessageText(
                text = message.content,
                textColor = Color.White,
                date = message.date,
                backgroundColor = MaterialTheme.colorScheme.primary,
                dateTimeTextColor = dateTimeTextColor,
                onClick = onClick
            )

            if (showSeen) {
                val seenColor = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray

                Text(
                    modifier = Modifier.padding(
                        top = MaterialTheme.spacing.extraSmall,
                        end = MaterialTheme.spacing.smallMedium
                    ),
                    text = stringResource(id = R.string.message_seen),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light),
                    color = seenColor
                )
            }
        }

        if (message.state == MessageState.SENDING) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = stringResource(id = R.string.send_message_icon_description),
                tint = if (isSystemInDarkTheme()) Color.Gray else Color.LightGray,
                modifier = Modifier.size(20.dp).weight(0.1f)
            )
        }

        AnimatedVisibility(
            modifier = Modifier.weight(0.1f),
            visible = message.state == MessageState.ERROR
        ) {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_error),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
fun ReceiveMessageItem(
    modifier: Modifier = Modifier,
    profilePictureUrl: String?,
    message: Message,
    displayProfilePicture: Boolean
) {
    val foreground = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.white else MaterialTheme.colorScheme.black

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        verticalAlignment = Alignment.Bottom
    ) {
        if (displayProfilePicture) {
            ProfilePicture(url = profilePictureUrl, scale = 0.3f)
        } else {
            ProfilePicture(modifier = Modifier.alpha(0f), url = null, scale = 0.3f)
        }

        MessageText(
            modifier = Modifier.weight(0.8f, fill = false),
            text = message.content,
            date = message.date,
            backgroundColor = MaterialTheme.colorScheme.inputBackground,
            textColor = foreground,
            dateTimeTextColor = Color(0xFF8E8E93)
        )

        Spacer(modifier = Modifier.weight(0.2f))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MessageText(
    modifier: Modifier = Modifier,
    text: String,
    date: LocalDateTime,
    textColor: Color,
    dateTimeTextColor: Color,
    backgroundColor: Color,
    onClick: (() -> Unit)? = null
) {
    FlowRow(
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.spacing.medium))
            .clickable(
                enabled = onClick != null,
                onClick = onClick ?: {}
            )
            .background(backgroundColor)
            .padding(
                vertical = MaterialTheme.spacing.small,
                horizontal = MaterialTheme.spacing.smallMedium
            ),
        horizontalArrangement = Arrangement.End
    ) {
        Text(
            modifier = Modifier.testTag(stringResource(R.string.chat_screen_message_text_tag)),
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )

        Text(
            modifier = Modifier
                .padding(start = MaterialTheme.spacing.small)
                .align(Alignment.Bottom),
            text = FormatLocalDateTimeUseCase.formatHourMinute(date),
            style = MaterialTheme.typography.labelSmall,
            color = dateTimeTextColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .clip(ShapeDefaults.ExtraLarge)
            .background(MaterialTheme.colorScheme.inputBackground)
            .padding(end = MaterialTheme.spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            modifier = modifier
                .weight(1f)
                .testTag(stringResource(R.string.chat_screen_message_input_tag)),
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.cursor),
            textStyle = TextStyle(color = MaterialTheme.colorScheme.cursor)
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.message_placeholder),
                        style = TextStyle(platformStyle = PlatformTextStyle(false)),
                        color = MaterialTheme.colorScheme.inputForeground
                    )
                },
                enabled = true,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                    unfocusedContainerColor = MaterialTheme.colorScheme.inputBackground,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.cursor
                ),
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.medium,
                    vertical = MaterialTheme.spacing.smallMedium
                )
            )
        }

        if (value.isNotBlank()) {
            Button(
                modifier = Modifier
                    .testTag(stringResource(R.string.chat_screen_send_button_tag)),
                onClick = onSendClick,
                contentPadding = PaddingValues()
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Send,
                    contentDescription = stringResource(id = R.string.send_message_icon_description),
                    tint = MaterialTheme.colorScheme.white
                )
            }
        }
    }
}

@Composable
fun NewMessageIndicator(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.smallMedium),
        Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.clickable { onClick() },
            shadowElevation = 2.dp,
            shape = ShapeDefaults.Small,
            color = Color.White
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        horizontal = MaterialTheme.spacing.large,
                        vertical = MaterialTheme.spacing.smallMedium
                    ),
                text = stringResource(id = R.string.new_message),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.black
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

private val smallText = "Bonsoir, pas de soucis."
private val mediumText = "Cela pourrait également aider à résoudre tout problème éventuel."
private val longtext = "Bonjour, j'espère que vous allez bien. " +
        "Je voulais prendre un moment pour vous parler de quelque chose d'important. " +
        "En fait, je pense qu'il est essentiel que nous discutions de la direction que prend notre projet, " +
        "car il y a plusieurs points que nous devrions clarifier. " +
        "Tout d'abord, j'ai remarqué que certains aspects de notre stratégie actuelle pourraient être améliorés. " +
        "Je crois que nous pourrions gagner en efficacité si nous ajustions certaines étapes du processus. " +
        "Par exemple, en ce qui concerne la gestion des priorités, il serait peut-être utile de revoir nos méthodes " +
        "afin d'être sûrs que nous concentrons nos efforts sur les éléments les plus importants."

@Preview
@Composable
private fun SentMessageItemPreview() {
    GedoiseTheme {
        Column {
            SentMessageItem(
                message = messageFixture.copy(content = mediumText),
                showSeen = true
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            SentMessageItem(message = messageFixture.copy(state = MessageState.ERROR))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            SentMessageItem(message = messageFixture.copy(state = MessageState.SENDING))
        }
    }
}

@Preview(widthDp = 400)
@Composable
private fun ReceiveMessageItemPreview() {
    GedoiseTheme {
        Column {
            ReceiveMessageItem(
                message = messageFixture.copy(content = mediumText),
                displayProfilePicture = true,
                profilePictureUrl = ""
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            ReceiveMessageItem(
                message = messageFixture,
                displayProfilePicture = false,
                profilePictureUrl = ""
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MessageTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    GedoiseTheme {
        MessageInput(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            onSendClick = { },
        )
    }
}

@Preview(widthDp = 200, heightDp = 100)
@Composable
private fun NewMessageIndicatorPreview() {
    GedoiseTheme {
        NewMessageIndicator {}
    }
}