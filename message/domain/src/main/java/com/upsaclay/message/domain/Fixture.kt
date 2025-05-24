package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import java.time.LocalDateTime
import java.time.ZoneOffset

val messageFixture = Message(
    id = 1,
    senderId = userFixture.id,
    recipientId = userFixture2.id,
    conversationId = "1",
    content = "Salut, bien et toi ? Oui bien sûr.",
    date = LocalDateTime.of(2024, 7, 20, 10, 0),
    seen = true,
    state = MessageState.SENT
)

val messageFixture2 = Message(
    id = 2,
    senderId = userFixture2.id,
    recipientId = userFixture2.id,
    conversationId = "1",
    content = "Salut ça va ? Cela fait longtemps que j'attend de te parler. Pourrait-on se voir ?",
    date = LocalDateTime.now(ZoneOffset.UTC),
    seen = false,
    state = MessageState.SENT
)

val messagesFixture = listOf(
    messageFixture.copy(id = 1),
    messageFixture2.copy(id = 2, date = LocalDateTime.now(ZoneOffset.UTC).minusDays(2)),
    messageFixture.copy(id = 3, date = LocalDateTime.now(ZoneOffset.UTC).minusDays(1)),
    messageFixture2.copy(id = 4, date = LocalDateTime.now(ZoneOffset.UTC)),
    messageFixture2.copy(id = 5, date = LocalDateTime.now(ZoneOffset.UTC)),
    messageFixture2.copy(id = 6, date = LocalDateTime.now(ZoneOffset.UTC)),
    messageFixture2.copy(id = 7, date = LocalDateTime.now(ZoneOffset.UTC)),
    messageFixture2.copy(id = 8, date = LocalDateTime.now(ZoneOffset.UTC)),
    messageFixture2.copy(id = 9, date = LocalDateTime.now(ZoneOffset.UTC)),
)

val conversationUiFixture = ConversationUi(
    id = "1",
    interlocutor = userFixture2,
    lastMessage = messageFixture,
    createdAt = LocalDateTime.of(2024, 7, 20, 10, 0),
    state = ConversationState.CREATED,
)

val conversationFixture = Conversation(
    id = "1",
    interlocutor = userFixture2,
    createdAt = LocalDateTime.of(2024, 7, 20, 10, 0),
    state = ConversationState.CREATED
)

val conversationMessageFixture = ConversationMessage(
    conversation = conversationFixture,
    lastMessage = messageFixture
)

val conversationsUIFixture = listOf(
    conversationUiFixture,
    conversationUiFixture.copy(id = "2", lastMessage = messageFixture.copy(date = messageFixture.date.minusMinutes(1))),
    conversationUiFixture.copy(id = "3", lastMessage = messageFixture.copy(date = messageFixture.date.minusMinutes(20))),
    conversationUiFixture.copy(id = "4", lastMessage = messageFixture.copy(date = messageFixture.date.minusHours(1))),
    conversationUiFixture.copy(id = "5", lastMessage = messageFixture.copy(date = messageFixture.date.minusHours(2))),
    conversationUiFixture.copy(id = "6", lastMessage = messageFixture.copy(date = messageFixture.date.minusDays(1))),
    conversationUiFixture.copy(id = "7", lastMessage = messageFixture.copy(date = messageFixture.date.minusDays(2))),
    conversationUiFixture.copy(id = "8", lastMessage = messageFixture.copy(date = messageFixture.date.minusWeeks(3))),
    conversationUiFixture.copy(id = "9", lastMessage = messageFixture.copy(date = messageFixture.date.minusMonths(1)))
)

val conversationsFixture = listOf(
    conversationFixture,
    conversationFixture.copy(
        createdAt = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(1),
    ),
    conversationFixture.copy(
        createdAt = LocalDateTime.now(ZoneOffset.UTC).minusMinutes(20),
    ),
    conversationFixture.copy(
        createdAt = LocalDateTime.now(ZoneOffset.UTC).minusHours(1),
    ),
    conversationFixture.copy(
        createdAt = LocalDateTime.now(ZoneOffset.UTC).minusHours(2),
    ),
    conversationFixture.copy(
        createdAt = LocalDateTime.now(ZoneOffset.UTC).minusDays(1)
    ),
    conversationFixture.copy(
        createdAt = LocalDateTime.now(ZoneOffset.UTC).minusDays(2)
    ),
    conversationFixture.copy(
        createdAt = LocalDateTime.now(ZoneOffset.UTC).minusWeeks(3)
    ),
    conversationFixture.copy(
       createdAt = LocalDateTime.now(ZoneOffset.UTC).minusMonths(1)
    )
)

val conversationsMessageFixture = listOf(
    conversationMessageFixture
)