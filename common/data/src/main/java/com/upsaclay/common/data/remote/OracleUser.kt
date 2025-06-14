package com.upsaclay.common.data.remote

import com.google.gson.annotations.SerializedName
import com.upsaclay.common.data.UserField.Oracle.USER_EMAIL
import com.upsaclay.common.data.UserField.Oracle.USER_FIRST_NAME
import com.upsaclay.common.data.UserField.Oracle.USER_IS_MEMBER
import com.upsaclay.common.data.UserField.Oracle.USER_LAST_NAME
import com.upsaclay.common.data.UserField.Oracle.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.common.data.UserField.Oracle.USER_SCHOOL_LEVEL
import com.upsaclay.common.data.UserField.Oracle.USER_ID

internal data class OracleUser(
    @SerializedName(USER_ID)
    val userId: String,
    @SerializedName(USER_FIRST_NAME)
    val userFirstName: String,
    @SerializedName(USER_LAST_NAME)
    val userLastName: String,
    @SerializedName(USER_EMAIL)
    val userEmail: String,
    @SerializedName(USER_SCHOOL_LEVEL)
    val userSchoolLevel: String,
    @SerializedName(USER_IS_MEMBER)
    val userIsMember: Int = 0,
    @SerializedName(USER_PROFILE_PICTURE_FILE_NAME)
    val userProfilePictureFileName: String? = null
)