package com.upsaclay.news.data

import com.upsaclay.common.domain.UrlUtils.formatProfilePictureUrl
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toLocalDateTime
import com.upsaclay.common.domain.extensions.toLong
import com.upsaclay.news.data.local.model.LocalAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncementWithUser
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState

internal object AnnouncementMapper {
    fun toLocal(announcement: Announcement) = LocalAnnouncement(
        announcementId = announcement.id,
        announcementTitle = announcement.title,
        announcementContent = announcement.content,
        announcementDate = announcement.date.toLong(),
        announcementState = announcement.state,
        userId = announcement.author.id,
        userFirstName = announcement.author.firstName,
        userLastName = announcement.author.lastName,
        userEmail = announcement.author.email,
        userSchoolLevel = announcement.author.schoolLevel,
        userIsMember = announcement.author.isMember,
        userProfilePictureFileName = announcement.author.profilePictureFileName
    )

    fun toDomain(localAnnouncement: LocalAnnouncement) = Announcement(
        id = localAnnouncement.announcementId,
        title = localAnnouncement.announcementTitle,
        content = localAnnouncement.announcementContent,
        date = localAnnouncement.announcementDate.toLocalDateTime(),
        author = User(
            id = localAnnouncement.userId,
            firstName = localAnnouncement.userFirstName,
            lastName = localAnnouncement.userLastName,
            email = localAnnouncement.userEmail,
            schoolLevel = localAnnouncement.userSchoolLevel,
            isMember = localAnnouncement.userIsMember,
            profilePictureFileName = localAnnouncement.userProfilePictureFileName
        ),
        state = localAnnouncement.announcementState
    )

    fun toDomain(remoteAnnouncement: RemoteAnnouncementWithUser) = Announcement(
        id = remoteAnnouncement.announcementId,
        title = remoteAnnouncement.announcementTitle,
        content = remoteAnnouncement.announcementContent,
        date = remoteAnnouncement.announcementDate.toLocalDateTime(),
        author = User(
            id = remoteAnnouncement.userId,
            firstName = remoteAnnouncement.userFirstName,
            lastName = remoteAnnouncement.userLastName,
            email = remoteAnnouncement.userEmail,
            schoolLevel = remoteAnnouncement.userSchoolLevel,
            isMember = remoteAnnouncement.userIsMember == 1,
            profilePictureFileName = formatProfilePictureUrl(remoteAnnouncement.profilePictureFileName)
        ),
        state = AnnouncementState.PUBLISHED
    )

    fun toRemote(announcement: Announcement) = RemoteAnnouncement(
        announcementId = announcement.id,
        announcementTitle = announcement.title,
        announcementContent = announcement.content,
        announcementDate = announcement.date.toLong(),
        userId = announcement.author.id
    )
}