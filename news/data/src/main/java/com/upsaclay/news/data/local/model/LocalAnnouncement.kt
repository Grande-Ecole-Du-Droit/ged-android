package com.upsaclay.news.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.news.domain.entity.AnnouncementState

const val ANNOUNCEMENTS_TABLE = "announcements_table"

@Entity(tableName = ANNOUNCEMENTS_TABLE)
data class LocalAnnouncement(
    @PrimaryKey
    @ColumnInfo(name = "announcementId")
    val announcementId: String,
    @ColumnInfo(name = "announcementTitle")
    val announcementTitle: String?,
    @ColumnInfo(name = "announcementContent")
    val announcementContent: String,
    @ColumnInfo(name = "announcementDate")
    val announcementDate: Long,
    @ColumnInfo(name = "announcementState")
    val announcementState: AnnouncementState,
    @ColumnInfo("userId")
    val userId: String,
    @ColumnInfo("userFirstName")
    val userFirstName: String,
    @ColumnInfo("userLastName")
    val userLastName: String,
    @ColumnInfo("userEmail")
    val userEmail: String,
    @ColumnInfo("userSchoolLevel")
    val userSchoolLevel: String,
    @ColumnInfo("userIsMember")
    val userIsMember: Boolean,
    @ColumnInfo("userProfilePictureFileName")
    val userProfilePictureFileName: String?
)