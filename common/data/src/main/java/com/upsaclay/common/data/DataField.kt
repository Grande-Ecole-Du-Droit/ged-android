package com.upsaclay.common.data

internal object UserField {
    internal object Local {
        const val USER_ID = "userId"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val EMAIL = "email"
        const val SCHOOL_LEVEL = "schoolLevel"
        const val IS_MEMBER = "isMember"
        const val PROFILE_PICTURE_FILE_NAME = "profilePictureFileName"
    }

    internal object Oracle {
        const val USER_ID = "USER_ID"
        const val USER_FIRST_NAME = "USER_FIRST_NAME"
        const val USER_LAST_NAME = "USER_LAST_NAME"
        const val USER_EMAIL = "USER_EMAIL"
        const val USER_SCHOOL_LEVEL = "USER_SCHOOL_LEVEL"
        const val USER_IS_MEMBER = "USER_IS_MEMBER"
        const val USER_PROFILE_PICTURE_FILE_NAME = "USER_PROFILE_PICTURE_FILE_NAME"
    }

    internal object Firestore {
        const val USER_ID = "userId"
        const val FIRST_NAME = "firstName"
        const val LAST_NAME = "lastName"
        const val EMAIL = "email"
        const val SCHOOL_LEVEL = "schoolLevel"
        const val IS_MEMBER = "isMember"
        const val PROFILE_PICTURE_FILE_NAME = "profilePictureFileName"
        const val FULL_NAME = "fullName"
    }
}