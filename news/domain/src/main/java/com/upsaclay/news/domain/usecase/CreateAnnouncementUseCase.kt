package com.upsaclay.news.domain.usecase

import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.repository.AnnouncementRepository

class CreateAnnouncementUseCase(private val announcementRepository: AnnouncementRepository) {
    suspend operator fun invoke(announcement: Announcement): Result<Int> = announcementRepository.createAnnouncement(announcement)
}