package com.upsaclay.message.data.remote.api

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField
import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

internal class ConversationApiImpl: ConversationApi {
    private val conversationsCollection = Firebase.firestore.collection(CONVERSATIONS_TABLE_NAME)

    override fun listenConversations(userId: String): Flow<RemoteConversation> = callbackFlow {
        val listener = conversationsCollection
            .whereArrayContains(ConversationField.Remote.PARTICIPANTS, userId)
            .addSnapshotListener { snapshot, error ->
                error?.let {
                    close(it)
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

    override suspend fun createConversation(conversationId: String, data: Map<String, Any>) {
        conversationsCollection
            .document(conversationId)
            .set(data, SetOptions.merge())
            .await()
    }

    override suspend fun updateConversation(conversationId: String, data: Map<String, Any>) {
        conversationsCollection
            .document(conversationId)
            .update(data)
            .await()
    }
}