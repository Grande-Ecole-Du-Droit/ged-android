package com.upsaclay.gedoise.presentation

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.graphics.createBitmap
import androidx.core.graphics.drawable.IconCompat
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.SystemEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toLong
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.usecase.GenerateRandomIdUseCase
import com.upsaclay.common.domain.usecase.SharedEventsUseCase
import com.upsaclay.gedoise.domain.repository.ScreenRepository
import com.upsaclay.message.domain.JsonConverter
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.presentation.chat.ChatRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val MESSAGE_CHANNEL_ID = "message_channel_id"

@SuppressLint("MissingPermission")
class NotificationPresenter(
    private val context: Context,
    private val imageRepository: ImageRepository,
    private val screenRepository: ScreenRepository,
    private val sharedEventsUseCase: SharedEventsUseCase
) {
    private val notificationManager = NotificationManagerCompat.from(context)
    private val scope = CoroutineScope(Dispatchers.Main)

    fun start() {
        listenSystemEvents()
        createMessageNotificationChannel()
    }

    suspend fun showMessageNotification(conversationMessage: ConversationMessage) {
        if (isCurrentMessageScreen(conversationMessage.conversation.id) ||
            !notificationManager.areNotificationsEnabled()
        ) {
            return
        }
        val message = conversationMessage.lastMessage
        val interlocutor = conversationMessage.conversation.interlocutor
        val intent = makeConversationIntent(conversationMessage.conversation)
        val userIcon = createUserIcon(interlocutor.profilePictureFileName)
        val user = buildPerson(interlocutor, userIcon)

        val notification = buildMessageNotification(
            interlocutor = interlocutor,
            message = message,
            conversationId = conversationMessage.conversation.id,
            person = user,
            intent = intent
        )

        notificationManager.notify(message.id, notification)
    }

    private fun listenSystemEvents() {
        scope.launch {
            sharedEventsUseCase.systemEvents.collect { event ->
                when (event) {
                    is SystemEvent.ClearNotifications -> clearNotifications(event.notificationGroupId)
                }
            }
        }
    }

    private fun clearNotifications(notificationGroupId: String) {
        notificationManager.activeNotifications.filter {
            it.groupKey == notificationGroupId
        }.forEach {
            notificationManager.cancel(it.id)
        }
    }

    private fun isCurrentMessageScreen(conversationId: String): Boolean {
        val messageScreen = screenRepository.currentRoute as? ChatRoute
        return messageScreen
            ?.conversationJson
            ?.let(JsonConverter::toConversation)
            ?.id == conversationId
    }

    private fun createMessageNotificationChannel() {
        val channel = NotificationChannel(
            MESSAGE_CHANNEL_ID,
            "Message",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Message notification"
        }

        notificationManager.createNotificationChannel(channel)
    }

    private fun getCircledBitmap(bitmap: Bitmap): Bitmap {
        val output = createBitmap(bitmap.width, bitmap.height)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(
            (bitmap.width / 2).toFloat(),
            (bitmap.height / 2).toFloat(),
            (bitmap.width / 2).toFloat(),
            paint
        )
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun makeConversationIntent(conversation: Conversation): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(MainActivity.CONVERSATION_ID_EXTRA, JsonConverter.fromConversation(conversation))
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        return PendingIntent.getActivity(
            context,
            GenerateRandomIdUseCase.intId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildPerson(interlocutor: User, icon: IconCompat): Person {
        return Person.Builder()
            .setIcon(icon)
            .setName(interlocutor.fullName)
            .build()
    }

    private suspend fun createUserIcon(profilePictureUrl: String?): IconCompat {
        val profilePicture = runCatching {
            profilePictureUrl?.let { imageRepository.getImage(it) }
        }.getOrNull()

        return profilePicture?.let {
            IconCompat.createWithBitmap(getCircledBitmap(it))
        } ?: IconCompat.createWithResource(context, R.drawable.default_profile_picture)
    }

    private fun buildMessageNotification(
        interlocutor: User,
        message: Message,
        conversationId: String,
        person: Person,
        intent: PendingIntent
    ): Notification {
        val messageKey = message.date.toLong().toString()

        val notificationBuilder = NotificationCompat.Builder(context, MESSAGE_CHANNEL_ID)
            .setContentTitle(interlocutor.fullName)
            .setContentText(message.content)
            .setSmallIcon(R.drawable.ic_notification)
            .setGroup(conversationId)
            .setSortKey(messageKey)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setContentIntent(intent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.MessagingStyle(person)
                    .addMessage(
                        message.content,
                        message.date.toLong(),
                        person
                    )
            )

        val newGroup = notificationManager.activeNotifications.none { it.notification.group == conversationId }

        if (newGroup) {
            notificationBuilder.setGroupSummary(true)
        }

        return notificationBuilder.build()
    }
}