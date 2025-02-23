package com.upsaclay.message.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.usecase.FormatLocalDateTimeUseCase
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.theme.GedoiseColor
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.messageFixture
import java.time.LocalDateTime

@Composable
fun SentMessageItem(
    modifier: Modifier = Modifier,
    message: Message,
    seen: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            Spacer(modifier = Modifier.fillMaxWidth(0.2f))

            MessageText(
                text = message.content,
                textColor = Color.White,
                date = message.date,
                backgroundColor = MaterialTheme.colorScheme.primary,
                dateTimeTextColor = Color(0xFFD1D3D8)
            )
        }

        if (seen) {
            Text(
                modifier = Modifier
                    .padding(
                        top = MaterialTheme.spacing.extraSmall,
                        end = MaterialTheme.spacing.smallMedium
                    ),
                text = stringResource(id = R.string.message_seen),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
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
    val background = if (isSystemInDarkTheme()) GedoiseColor.InputBackgroundDark else GedoiseColor.InputBackgroundLight
    val foreground = if (isSystemInDarkTheme()) GedoiseColor.White else GedoiseColor.Black

    Row(
        modifier = modifier.fillMaxWidth(0.8f),
        verticalAlignment = Alignment.Bottom
    ) {
        if (displayProfilePicture) {
            ProfilePicture(url = profilePictureUrl, scale = 0.3f)
        } else {
            ProfilePicture(modifier = Modifier.alpha(0f), url = null, scale = 0.3f)
        }

        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))

        MessageText(
            text = message.content,
            date = message.date,
            backgroundColor = background,
            textColor = foreground,
            dateTimeTextColor = Color(0xFF8E8E93)
        )
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
    backgroundColor: Color
) {
    FlowRow(
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.spacing.large))
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
    val colors: TextFieldColors = TextFieldDefaults.colors()
    val backgroundColor = if(isSystemInDarkTheme()) GedoiseColor.InputBackgroundDark else GedoiseColor.InputBackgroundLight
    val placeholderColor = if(isSystemInDarkTheme()) GedoiseColor.InputForegroundDark else GedoiseColor.InputForegroundLight
    val textColor = if (isSystemInDarkTheme()) GedoiseColor.White else GedoiseColor.Black

    Row(
        modifier = modifier
            .clip(ShapeDefaults.ExtraLarge)
            .background(backgroundColor)
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
            cursorBrush = SolidColor(colors.cursorColor),
            textStyle = TextStyle(color = textColor)
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.message_placeholder),
                        style = TextStyle(platformStyle = PlatformTextStyle(false)),
                        color = placeholderColor
                    )
                },
                enabled = true,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = backgroundColor,
                    unfocusedContainerColor = backgroundColor,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
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
                    tint = GedoiseColor.White
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun SentMessageItemPreview() {
    GedoiseTheme {
        Column(verticalArrangement = Arrangement.SpaceAround) {
            SentMessageItem(message = messageFixture, seen = true)

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            SentMessageItem(message = messageFixture.copy(state = MessageState.ERROR))

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))

            SentMessageItem(message = messageFixture.copy(state = MessageState.LOADING))
        }
    }
}

@Preview(widthDp = 360)
@Composable
private fun ReceiveMessageItemPreview() {
    GedoiseTheme {
        ReceiveMessageItem(
            message = messageFixture.copy(content = mediumText),
            displayProfilePicture = true,
            profilePictureUrl = ""
        )
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