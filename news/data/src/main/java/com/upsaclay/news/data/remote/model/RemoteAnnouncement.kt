package com.upsaclay.news.data.remote.model

import com.google.gson.annotations.SerializedName
import com.upsaclay.news.data.AnnouncementField.Remote.ANNOUNCEMENT_CONTENT
import com.upsaclay.news.data.AnnouncementField.Remote.ANNOUNCEMENT_DATE
import com.upsaclay.news.data.AnnouncementField.Remote.ANNOUNCEMENT_ID
import com.upsaclay.news.data.AnnouncementField.Remote.ANNOUNCEMENT_TITLE
import com.upsaclay.news.data.AnnouncementField.Remote.USER_EMAIL
import com.upsaclay.news.data.AnnouncementField.Remote.USER_FIRST_NAME
import com.upsaclay.news.data.AnnouncementField.Remote.USER_ID
import com.upsaclay.news.data.AnnouncementField.Remote.USER_IS_MEMBER
import com.upsaclay.news.data.AnnouncementField.Remote.USER_LAST_NAME
import com.upsaclay.news.data.AnnouncementField.Remote.USER_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.news.data.AnnouncementField.Remote.USER_SCHOOL_LEVEL

internal data class RemoteAnnouncement(
    @SerializedName(ANNOUNCEMENT_ID)
    val announcementId: String,
    @SerializedName(ANNOUNCEMENT_TITLE)
    val announcementTitle: String?,
    @SerializedName(ANNOUNCEMENT_CONTENT)
    val announcementContent: String,
    @SerializedName(ANNOUNCEMENT_DATE)
    val announcementDate: Long,
    @SerializedName(USER_ID)
    val userId: String
)

internal data class RemoteAnnouncementWithUser(
    @SerializedName(ANNOUNCEMENT_ID)
    val announcementId: String,
    @SerializedName(ANNOUNCEMENT_TITLE)
    val announcementTitle: String?,
    @SerializedName(ANNOUNCEMENT_CONTENT)
    val announcementContent: String,
    @SerializedName(ANNOUNCEMENT_DATE)
    val announcementDate: Long,
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
    val userIsMember: Int,
    @SerializedName(USER_PROFILE_PICTURE_FILE_NAME)
    val profilePictureFileName: String?
)