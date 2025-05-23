package com.upsaclay.common.data

import com.upsaclay.common.data.local.UserDTO
import com.upsaclay.common.data.remote.FirestoreUser
import com.upsaclay.common.domain.UrlUtils.formatProfilePictureUrl
import com.upsaclay.common.domain.UrlUtils.getFileNameFromUrl
import com.upsaclay.common.domain.entity.User

internal fun User.toDTO() = UserDTO(
    userId = id,
    userFirstName = firstName,
    userLastName = lastName,
    userEmail = email,
    userSchoolLevel = schoolLevel,
    userIsMember = if (isMember) 1 else 0,
    userProfilePictureFileName = getFileNameFromUrl(profilePictureFileName)
)

internal fun User.toFirestoreUser() = FirestoreUser(
    userId = id,
    firstName = firstName,
    lastName = lastName,
    fullName = "$firstName $lastName",
    email = email,
    schoolLevel = schoolLevel,
    isMember = isMember,
    profilePictureFileName = getFileNameFromUrl(profilePictureFileName)
)

internal fun UserDTO.toUser() = User(
    id = userId,
    firstName = userFirstName,
    lastName = userLastName,
    email = userEmail,
    schoolLevel = userSchoolLevel,
    isMember = userIsMember == 1,
    profilePictureFileName = formatProfilePictureUrl(userProfilePictureFileName)
)

internal fun FirestoreUser.toUser() = User(
    id = userId,
    firstName = firstName,
    lastName = lastName,
    email = email,
    schoolLevel = schoolLevel,
    isMember = isMember,
    profilePictureFileName = formatProfilePictureUrl(profilePictureFileName)
)
