package com.upsaclay.common.data.local

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField.Local.EMAIL
import com.upsaclay.common.data.UserField.Local.FIRST_NAME
import com.upsaclay.common.data.UserField.Local.IS_MEMBER
import com.upsaclay.common.data.UserField.Local.LAST_NAME
import com.upsaclay.common.data.UserField.Local.PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.UserField.Local.SCHOOL_LEVEL
import com.upsaclay.common.data.UserField.Local.USER_ID

internal data class LocalUser(
    @SerializedName(USER_ID)
    val userId: String,
    @SerializedName(FIRST_NAME)
    val userFirstName: String,
    @SerializedName(LAST_NAME)
    val userLastName: String,
    @SerializedName(EMAIL)
    val userEmail: String,
    @SerializedName(SCHOOL_LEVEL)
    val userSchoolLevel: String,
    @SerializedName(IS_MEMBER)
    val userIsMember: Int = 0,
    @SerializedName(PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String? = null
)