package com.upsaclay.common.data.local

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField

internal data class LocalUser(
    @SerializedName(UserField.Local.USER_ID)
    val userId: String? = null,
    @SerializedName(UserField.Local.USER_FIRST_NAME)
    val userFirstName: String,
    @SerializedName(UserField.Local.USER_LAST_NAME)
    val userLastName: String,
    @SerializedName(UserField.Local.USER_EMAIL)
    val userEmail: String,
    @SerializedName(UserField.Local.USER_SCHOOL_LEVEL)
    val userSchoolLevel: String,
    @SerializedName(UserField.Local.USER_IS_MEMBER)
    val userIsMember: Int = 0,
    @SerializedName(UserField.Local.USER_PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String? = null
)