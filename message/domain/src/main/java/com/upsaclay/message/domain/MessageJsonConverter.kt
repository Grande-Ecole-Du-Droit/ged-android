package com.upsaclay.message.domain

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.LocalDateTimeAdapter
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import java.time.LocalDateTime

object MessageJsonConverter {
    fun toConversation(conversationJson: String): Conversation? {
        return runCatching {
            GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
                .create()
                .fromJson(conversationJson, Conversation::class.java)
        }.getOrNull()
    }

    fun fromConversation(conversation: Conversation): String {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
            .create()
            .toJson(conversation)
    }

    fun fromConversationMessage(conversationMessageJson: String): ConversationMessage? {
        return runCatching {
            GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
                .create()
                .fromJson(conversationMessageJson, ConversationMessage::class.java)
        }.getOrNull()
    }
}