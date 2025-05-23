package com.upsaclay.common.data.local

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField

internal data class UserDTO(
    @SerializedName(UserField.USER_ID)
    val userId: String,
    @SerializedName(UserField.FIRST_NAME)
    val userFirstName: String,
    @SerializedName(UserField.LAST_NAME)
    val userLastName: String,
    @SerializedName(UserField.EMAIL)
    val userEmail: String,
    @SerializedName(UserField.SCHOOL_LEVEL)
    val userSchoolLevel: String,
    @SerializedName(UserField.IS_MEMBER)
    val userIsMember: Int = 0,
    @SerializedName(UserField.PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String? = null
)