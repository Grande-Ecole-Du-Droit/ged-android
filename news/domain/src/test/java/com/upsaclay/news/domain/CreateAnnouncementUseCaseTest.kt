package com.upsaclay.news.domain

import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()

    private lateinit var useCase: CreateAnnouncementUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { announcementRepository.createAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.updateAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.updateLocalAnnouncement(any()) } returns Unit

        useCase = CreateAnnouncementUseCase(
            announcementRepository = announcementRepository,
            scope = testScope
        )
    }

    @Test
    fun createAnnouncement_should_create_announcement_with_publishing_state() {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
        }
    }

    @Test
    fun createAnnouncement_should_update_local_announcement_to_published_state_when_succeeds() {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
        }
    }

    @Test
    fun createAnnouncement_should_update_local_announcement_to_error_state_when_fails() {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.DRAFT)
        coEvery { announcementRepository.createAnnouncement(any()) } throws Exception()

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
        }
    }
}