package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.upsaclay.common.domain.e
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class ConversationApiImpl: ConversationApi {
    private val conversationsCollection = Firebase.firestore.collection(CONVERSATIONS_TABLE_NAME)

    override fun listenConversations(userId: String): Flow<RemoteConversation> = callbackFlow {
        val listener = conversationsCollection
            .whereArrayContains(ConversationField.Remote.PARTICIPANTS, userId)
            .whereEqualTo("${ConversationField.Remote.DELETE_BY}.$userId", false)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    e("Error getting conversations", it)
                    return@addSnapshotListener
                }

                snapshot?.documents?.forEach { document ->
                    document.toObject(RemoteConversation::class.java)?.let {
                        trySend(it)
                    }
                }
            }

        awaitClose { listener.remove() }
    }

    override suspend fun getConversation(conversationId: String): RemoteConversation? {
        return suspendCoroutine { continuation ->
            conversationsCollection
                .document(conversationId)
                .get()
                .addOnSuccessListener { continuation.resume(it.toObject(RemoteConversation::class.java)) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun createConversation(conversationId: String, data: Map<String, Any>) {
        suspendCoroutine { continuation ->
            conversationsCollection
                .document(conversationId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun updateConversation(conversationId: String, data: Map<String, Any>) {
        suspendCoroutine { continuation ->
            conversationsCollection
                .document(conversationId)
                .update(data)
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }

    override suspend fun hardDeleteConversation(conversationId: String) {
        suspendCoroutine { continuation ->
            conversationsCollection
                .document(conversationId)
                .delete()
                .addOnSuccessListener { continuation.resume(Unit) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}