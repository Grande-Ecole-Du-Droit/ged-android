package com.upsaclay.common.data.remote

import com.google.firebase.firestore.PropertyName
import com.upsaclay.common.data.UserField

internal data class FirestoreUser(
    @get:PropertyName(UserField.USER_ID)
    @set:PropertyName(UserField.USER_ID)
    var userId: String = "",

    @get:PropertyName(UserField.FIRST_NAME)
    @set:PropertyName(UserField.FIRST_NAME)
    var firstName: String = "",

    @get:PropertyName(UserField.LAST_NAME)
    @set:PropertyName(UserField.LAST_NAME)
    var lastName: String = "",

    @get:PropertyName(UserField.Remote.FULL_NAME)
    @set:PropertyName(UserField.Remote.FULL_NAME)
    var fullName: String = "",

    @get:PropertyName(UserField.EMAIL)
    @set:PropertyName(UserField.EMAIL)
    var email: String = "",

    @get:PropertyName(UserField.SCHOOL_LEVEL)
    @set:PropertyName(UserField.SCHOOL_LEVEL)
    var schoolLevel: String = "",

    @get:PropertyName(UserField.IS_MEMBER)
    @set:PropertyName(UserField.IS_MEMBER)
    var isMember: Boolean = false,

    @get:PropertyName(UserField.PROFILE_PICTURE_FILE_NAME)
    @set:PropertyName(UserField.PROFILE_PICTURE_FILE_NAME)
    var profilePictureFileName: String? = null
)