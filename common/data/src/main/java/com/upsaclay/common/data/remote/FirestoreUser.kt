package com.upsaclay.common.data.remote

import com.google.firebase.firestore.PropertyName
import com.upsaclay.common.data.UserField.Firestore.EMAIL
import com.upsaclay.common.data.UserField.Firestore.FIRST_NAME
import com.upsaclay.common.data.UserField.Firestore.FULL_NAME
import com.upsaclay.common.data.UserField.Firestore.IS_MEMBER
import com.upsaclay.common.data.UserField.Firestore.LAST_NAME
import com.upsaclay.common.data.UserField.Firestore.PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.UserField.Firestore.SCHOOL_LEVEL
import com.upsaclay.common.data.UserField.Firestore.USER_ID

internal data class FirestoreUser(
    @get:PropertyName(USER_ID)
    @set:PropertyName(USER_ID)
    var userId: String = "",

    @get:PropertyName(FIRST_NAME)
    @set:PropertyName(FIRST_NAME)
    var firstName: String = "",

    @get:PropertyName(LAST_NAME)
    @set:PropertyName(LAST_NAME)
    var lastName: String = "",

    @get:PropertyName(FULL_NAME)
    @set:PropertyName(FULL_NAME)
    var fullName: String = "",

    @get:PropertyName(EMAIL)
    @set:PropertyName(EMAIL)
    var email: String = "",

    @get:PropertyName(SCHOOL_LEVEL)
    @set:PropertyName(SCHOOL_LEVEL)
    var schoolLevel: String = "",

    @get:PropertyName(IS_MEMBER)
    @set:PropertyName(IS_MEMBER)
    var isMember: Boolean = false,

    @get:PropertyName(PROFILE_PICTURE_FILE_NAME)
    @set:PropertyName(PROFILE_PICTURE_FILE_NAME)
    var profilePictureFileName: String? = null
)