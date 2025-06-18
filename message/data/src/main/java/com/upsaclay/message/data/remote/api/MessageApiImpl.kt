package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.firestore
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.model.MessageField
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.data.remote.withOffsetTime
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class MessageApiImpl : MessageApi {
    private val conversationsCollection = Firebase.firestore.collection(CONVERSATIONS_TABLE_NAME)

    override fun listenMessages(conversationId: String, interlocutorId: String, offsetTime: Timestamp?): Flow<RemoteMessage> = callbackFlow {
        val listener = conversationsCollection
            .document(conversationId)
            .collection(MESSAGES_TABLE_NAME)
            .withOffsetTime(offsetTime)
            .addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
                error?.let {
                    close(it)
                    return@addSnapshotListener
                }

                snapshot?.documents
                    ?.filterNot { it.metadata.isFromCache || it.metadata.hasPendingWrites() }
                    ?.forEach { document ->
                    document.toObject(RemoteMessage::class.java)?.let {
                        trySend(it)
                    }
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun createMessage(remoteMessage: RemoteMessage) {
        conversationsCollection
            .document(remoteMessage.conversationId)
            .collection(MESSAGES_TABLE_NAME)
            .document(remoteMessage.messageId.toString())
            .set(remoteMessage)
            .await()
    }

    override suspend fun updateSeenMessage(remoteMessage: RemoteMessage) {
        conversationsCollection
            .document(remoteMessage.conversationId)
            .collection(MESSAGES_TABLE_NAME)
            .document(remoteMessage.messageId.toString())
            .update(MessageField.SEEN, remoteMessage.seen)
            .await()
    }
}