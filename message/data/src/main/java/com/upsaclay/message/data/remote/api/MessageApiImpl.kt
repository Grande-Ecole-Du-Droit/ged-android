package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.firestore
import com.upsaclay.common.domain.e
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.MESSAGES_TABLE_NAME
import com.upsaclay.message.data.model.MessageField
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class MessageApiImpl : MessageApi {
    private val conversationsCollection = Firebase.firestore.collection(CONVERSATIONS_TABLE_NAME)

    override fun listenMessages(conversationId: String, offsetTime: Timestamp?): Flow<List<RemoteMessage>> = callbackFlow {
        val query = conversationsCollection
            .document(conversationId)
            .collection(MESSAGES_TABLE_NAME)
            .withOffsetTime(offsetTime)

        val listener = query.addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
            error?.let {
                e("Error getting last messages", it)
                return@addSnapshotListener
            }

            val messages = snapshot?.documents
                ?.filterNot { it.metadata.isFromCache || it.metadata.hasPendingWrites() }
                ?.mapNotNull { document ->
                    document.toObject(RemoteMessage::class.java)
                }

            messages?.let { trySend(it) }
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

    override suspend fun deleteMessages(conversationId: String) {
        suspendCancellableCoroutine { continuation ->
            conversationsCollection
                .document(conversationId)
                .collection(MESSAGES_TABLE_NAME)
                .get(Source.SERVER)
                .addOnSuccessListener { messageSnapshot ->
                    messageSnapshot.documents.forEach { document ->
                        document.reference.delete()
                    }
                    continuation.resume(Unit)
                }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    private fun CollectionReference.withOffsetTime(offsetTime: Timestamp?): Query {
        return offsetTime?.let {
            whereGreaterThan(MessageField.TIMESTAMP, it)
        } ?: this
    }
}