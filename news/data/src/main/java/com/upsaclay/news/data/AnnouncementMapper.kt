package com.upsaclay.news.data

import com.upsaclay.common.domain.UrlUtils.formatProfilePictureUrl
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.news.data.local.model.LocalAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncement
import com.upsaclay.news.data.remote.model.RemoteAnnouncementWithUser
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState

fun Announcement.toLocal() = LocalAnnouncement(
    announcementId = id,
    announcementTitle = title,
    announcementContent = content,
    announcementDate = date.toEpochMilliUTC(),
    announcementState = state,
    userId = author.id,
    userFirstName = author.firstName,
    userLastName = author.lastName,
    userEmail = author.email,
    userSchoolLevel = author.schoolLevel,
    userIsMember = author.isMember,
    userProfilePictureFileName = author.profilePictureUrl
)

fun LocalAnnouncement.toAnnouncement() = Announcement(
    id = announcementId,
    title = announcementTitle,
    content = announcementContent,
    date = announcementDate.toLocalDateTimeUTC(),
    author = User(
        id = userId,
        firstName = userFirstName,
        lastName = userLastName,
        email = userEmail,
        schoolLevel = userSchoolLevel,
        isMember = userIsMember,
        profilePictureUrl = userProfilePictureFileName
    ),
    state = announcementState
)

internal fun RemoteAnnouncementWithUser.toAnnouncement() = Announcement(
    id = announcementId,
    title = announcementTitle,
    content = announcementContent,
    date = announcementDate.toLocalDateTimeUTC(),
    author = User(
        id = userId,
        firstName = userFirstName,
        lastName = userLastName,
        email = userEmail,
        schoolLevel = userSchoolLevel,
        isMember = userIsMember == 1,
        profilePictureUrl = formatProfilePictureUrl(profilePictureFileName)
    ),
    state = AnnouncementState.PUBLISHED
)

internal fun Announcement.toRemote() = RemoteAnnouncement(
    announcementId = id,
    announcementTitle = title,
    announcementContent = content,
    announcementDate = date.toEpochMilliUTC(),
    userId = author.id
)
