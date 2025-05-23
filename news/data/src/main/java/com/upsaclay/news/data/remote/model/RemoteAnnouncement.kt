package com.upsaclay.news.data.remote.model

import com.google.gson.annotations.SerializedName

internal data class RemoteAnnouncement(
    @SerializedName("announcementId")
    val announcementId: String,
    @SerializedName("announcementTitle")
    val announcementTitle: String?,
    @SerializedName("announcementContent")
    val announcementContent: String,
    @SerializedName("announcementDate")
    val announcementDate: Long,
    @SerializedName("userId")
    val userId: String
)

internal data class RemoteAnnouncementWithUser(
    @SerializedName("announcementId")
    val announcementId: String,
    @SerializedName("announcementTitle")
    val announcementTitle: String?,
    @SerializedName("announcementContent")
    val announcementContent: String,
    @SerializedName("announcementDate")
    val announcementDate: Long,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("userFirstName")
    val userFirstName: String,
    @SerializedName("userLastName")
    val userLastName: String,
    @SerializedName("userEmail")
    val userEmail: String,
    @SerializedName("userSchoolLevel")
    val userSchoolLevel: String,
    @SerializedName("userIsMember")
    val userIsMember: Int,
    @SerializedName("userProfilePictureFileName")
    val profilePictureFileName: String?
)