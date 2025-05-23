package com.upsaclay.common.domain

import com.upsaclay.common.domain.entity.FcmData
import com.upsaclay.common.domain.entity.FcmDataType
import com.upsaclay.common.domain.entity.FcmMessage
import com.upsaclay.common.domain.entity.FcmNotification
import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.entity.User

val userFixture = User(
    "12",
    "Pierre",
    "Dupont",
    "pierre.dupont@universite-paris-saclay.fr",
    "GED 1",
    true,
    "https://i-mom.unimedias.fr/2020/09/16/dragon-ball-songoku.jpg"
)

val userFixture2 = User(
    "13",
    "Alain",
    "Robert",
    "alain.robert@universite-paris-saclay.fr",
    "GED 3",
    false,
    "https://avatarfiles.alphacoders.com/330/330775.png"
)

val usersFixture = listOf(
    userFixture,
    userFixture,
    userFixture,
    userFixture,
    userFixture2,
    userFixture2,
    userFixture2,
    userFixture2,
    userFixture2
)

val fcmMessageFixture = FcmMessage(
    recipientId = "1",
    notification = FcmNotification(
        title = "Test",
        body = "Test body"
    ),
    data = FcmData(
        type = FcmDataType.MESSAGE,
        value = "Test value"
    )
)

val fcmTokenFixture = FcmToken(userFixture.id, "token")