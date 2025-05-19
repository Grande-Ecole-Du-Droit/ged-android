package com.upsaclay.news.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.news.domain.entity.AnnouncementState

const val ANNOUNCEMENTS_TABLE = "announcements_table"

@Entity(tableName = ANNOUNCEMENTS_TABLE)
data class LocalAnnouncement(
    @PrimaryKey
    @ColumnInfo(name = "ANNOUNCEMENT_ID")
    val announcementId: String,
    @ColumnInfo(name = "ANNOUNCEMENT_TITLE")
    val announcementTitle: String?,
    @ColumnInfo(name = "ANNOUNCEMENT_CONTENT")
    val announcementContent: String,
    @ColumnInfo(name = "ANNOUNCEMENT_DATE")
    val announcementDate: Long,
    @ColumnInfo(name = "ANNOUNCEMENT_STATE")
    val announcementState: AnnouncementState,
    @ColumnInfo("USER_ID")
    val userId: String,
    @ColumnInfo("USER_FIRST_NAME")
    val userFirstName: String,
    @ColumnInfo("USER_LAST_NAME")
    val userLastName: String,
    @ColumnInfo("USER_EMAIL")
    val userEmail: String,
    @ColumnInfo("USER_SCHOOL_LEVEL")
    val userSchoolLevel: String,
    @ColumnInfo("USER_IS_MEMBER")
    val userIsMember: Boolean,
    @ColumnInfo("USER_PROFILE_PICTURE_FILE_NAME")
    val userProfilePictureFileName: String?
)