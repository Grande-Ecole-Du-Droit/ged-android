package com.upsaclay.common.data

import com.upsaclay.common.data.local.LocalUser
import com.upsaclay.common.data.remote.FirestoreUser
import com.upsaclay.common.domain.UrlUtils.formatProfilePictureUrl
import com.upsaclay.common.domain.UrlUtils.getFileNameFromUrl
import com.upsaclay.common.domain.entity.User

internal object UserMapper {
    fun toDTO(user: User) = LocalUser(
        userId = if (user.id == "") null else user.id,
        userFirstName = user.firstName,
        userLastName = user.lastName,
        userEmail = user.email,
        userSchoolLevel = user.schoolLevel,
        userIsMember = if (user.isMember) 1 else 0,
        userProfilePictureFileName = getFileNameFromUrl(user.profilePictureFileName)
    )

    fun toFirestoreUser(user: User) = FirestoreUser(
        userId = user.id,
        firstName = user.firstName,
        lastName = user.lastName,
        fullName = user.firstName + " " + user.lastName,
        email = user.email,
        schoolLevel = user.schoolLevel,
        isMember = user.isMember,
        profilePictureFileName = getFileNameFromUrl(user.profilePictureFileName)
    )

    fun toDomain(localUser: LocalUser) = User(
        id = localUser.userId ?: "",
        firstName = localUser.userFirstName,
        lastName = localUser.userLastName,
        email = localUser.userEmail,
        schoolLevel = localUser.userSchoolLevel,
        isMember = localUser.userIsMember == 1,
        profilePictureFileName = formatProfilePictureUrl(localUser.userProfilePictureFileName)
    )

    fun toDomain(firestoreUser: FirestoreUser) = User(
        id = firestoreUser.userId,
        firstName = firestoreUser.firstName,
        lastName = firestoreUser.lastName,
        email = firestoreUser.email,
        schoolLevel = firestoreUser.schoolLevel,
        isMember = firestoreUser.isMember,
        profilePictureFileName = formatProfilePictureUrl(firestoreUser.profilePictureFileName)
    )
}