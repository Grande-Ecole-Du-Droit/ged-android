package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.model.MessageField
import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

internal class MessageApiImpl : MessageApi {
    private val conversationsCollection = Firebase.firestore.collection(CONVERSATIONS_TABLE_NAME)

    override fun listenMessages(conversationId: String, offsetTime: Timestamp?): Flow<RemoteMessage> = callbackFlow {
        val query = conversationsCollection
            .document(conversationId)
            .collection(MESSAGES_TABLE_NAME)
            .withOffsetTime(offsetTime)

        val listener = query.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
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
        suspendCancellableCoroutine { continuation ->
            conversationsCollection
                .document(remoteMessage.conversationId)
                .collection(MESSAGES_TABLE_NAME)
                .document(remoteMessage.messageId.toString())
                .set(remoteMessage)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun updateSeenMessage(remoteMessage: RemoteMessage) {
        suspendCancellableCoroutine { continuation ->
            conversationsCollection
                .document(remoteMessage.conversationId)
                .collection(MESSAGES_TABLE_NAME)
                .document(remoteMessage.messageId.toString())
                .update(MessageField.SEEN, remoteMessage.seen)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
            }
    }

    private fun CollectionReference.withOffsetTime(offsetTime: Timestamp?): Query {
        return offsetTime?.let {
            whereGreaterThan(MessageField.TIMESTAMP, it)
        } ?: this
    }
}