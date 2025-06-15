package com.upsaclay.news.domain

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResendAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: ResendAnnouncementUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { connectivityObserver.isConnected } returns true
        coEvery { announcementRepository.refreshAnnouncements() } returns Unit

        useCase = ResendAnnouncementUseCase(
            announcementRepository = announcementRepository,
            connectivityObserver = connectivityObserver,
            scope = testScope
        )
    }

    @Test
    fun resendAnnouncement_should_create_announcement_with_publishing_state() {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
        }
    }

    @Test
    fun resendAnnouncement_should_update_local_announcement_to_published_state_when_succeeds() {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
        }
    }

    @Test
    fun resendAnnouncement_should_update_local_announcement_to_error_state_when_fails() {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.DRAFT)
        coEvery { announcementRepository.createAnnouncement(any()) } throws Exception()

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.updateLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
        }
    }

    @Test(expected = NoInternetConnectionException::class)
    fun resendAnnouncement_should_throw_NoInternetConnectionException_when_not_connected() {
        // Given
        every { connectivityObserver.isConnected } returns false
        val announcement = announcementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)
    }
}