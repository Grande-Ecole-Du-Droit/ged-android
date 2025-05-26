package com.upsaclay.news.domain

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.LocalDateTimeAdapter
import com.upsaclay.news.domain.entity.Announcement
import java.time.LocalDateTime

object NewsJsonConverter {
    fun toAnnouncement(announcementJson: String): Announcement? {
        return runCatching {
            GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
                .create()
                .fromJson(announcementJson, Announcement::class.java)
        }.getOrNull()
    }

    fun fromAnnouncement(announcement: Announcement): String {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
            .create()
            .toJson(announcement)
    }
}