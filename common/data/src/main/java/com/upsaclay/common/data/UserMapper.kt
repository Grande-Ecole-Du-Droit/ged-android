package com.upsaclay.common.data

import com.upsaclay.common.data.local.LocalUser
import com.upsaclay.common.data.remote.FirestoreUser
import com.upsaclay.common.domain.UrlUtils.formatProfilePictureUrl
import com.upsaclay.common.domain.UrlUtils.getFileNameFromUrl
import com.upsaclay.common.domain.entity.User

internal fun User.toLocal() = LocalUser(
    userId = id,
    userFirstName = firstName,
    userLastName = lastName,
    userEmail = email,
    userSchoolLevel = schoolLevel,
    userIsMember = if (isMember) 1 else 0,
    userProfilePictureFileName = getFileNameFromUrl(profilePictureUrl)
)

internal fun User.toFirestoreUser() = FirestoreUser(
    userId = id,
    firstName = firstName,
    lastName = lastName,
    fullName = "$firstName $lastName",
    email = email,
    schoolLevel = schoolLevel,
    isMember = isMember,
    profilePictureFileName = getFileNameFromUrl(profilePictureUrl)
)

internal fun LocalUser.toUser() = User(
    id = userId,
    firstName = userFirstName,
    lastName = userLastName,
    email = userEmail,
    schoolLevel = userSchoolLevel,
    isMember = userIsMember == 1,
    profilePictureUrl = formatProfilePictureUrl(userProfilePictureFileName)
)

internal fun FirestoreUser.toUser() = User(
    id = userId,
    firstName = firstName,
    lastName = lastName,
    email = email,
    schoolLevel = schoolLevel,
    isMember = isMember,
    profilePictureUrl = formatProfilePictureUrl(profilePictureFileName)
)
