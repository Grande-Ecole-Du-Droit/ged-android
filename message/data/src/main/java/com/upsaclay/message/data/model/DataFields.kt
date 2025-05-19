package com.upsaclay.message.data.model

internal object ConversationField {
    const val CONVERSATION_ID = "conversationId"
    const val CREATED_AT = "created_at"

    object Remote {
        const val PARTICIPANTS = "participants"
        const val DELETE_BY = "deleteBy"
        const val DELETE_TIME = "deleteTime"
    }

    object Local {
        const val INTERLOCUTOR_ID = "interlocutorId"
        const val INTERLOCUTOR_FIRST_NAME = "interlocutorFirstName"
        const val INTERLOCUTOR_LAST_NAME = "interlocutorLastName"
        const val INTERLOCUTOR_EMAIL = "interlocutorEmail"
        const val INTERLOCUTOR_SCHOOL_LEVEL = "interlocutorSchoolLevel"
        const val INTERLOCUTOR_IS_MEMBER = "interlocutorIsMember"
        const val INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME = "interlocutorProfilePictureFileName"
        const val CONVERSATION_STATE = "conversationState"
        const val CONVERSATION_DELETE_TIME = "conversationDeleteTime"
    }
}

internal object MessageField {
    const val MESSAGE_ID = "messageId"
    const val CONVERSATION_ID = "conversationId"
    const val SENDER_ID = "senderId"
    const val RECIPIENT_ID = "recipientId"
    const val CONTENT = "content"
    const val TIMESTAMP = "timestamp"
    const val SEEN = "seen"

    object Local {
        const val STATE = "messageState"
    }
}