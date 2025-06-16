package com.upsaclay.news.domain

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: RefreshAnnouncementUseCase

    @Before
    fun setUp() {
        every { connectivityObserver.isConnected } returns true
        coEvery { announcementRepository.refreshAnnouncements() } returns Unit

        useCase = RefreshAnnouncementUseCase(
            announcementRepository = announcementRepository,
            connectivityObserver = connectivityObserver
        )
    }

    @Test
    fun refreshAnnouncement_should_refresh_when_debounce_interval_exceeded() = runTest {
        // Given
        coEvery { announcementRepository.refreshAnnouncements() } returns Unit

        // When
        useCase()

        // Then
        coVerify { announcementRepository.refreshAnnouncements() }
    }

    @Test
    fun refreshAnnouncement_should_not_refresh_when_debounce_interval_not_exceeded() = runTest {
        // Given
        useCase.lastRequestTime = System.currentTimeMillis()

        // When
        useCase()

        // Then
        coVerify(exactly = 0) { announcementRepository.refreshAnnouncements() }
    }

    @Test(expected = NoInternetConnectionException::class)
    fun refreshAnnouncement_should_throw_NoInternetConnectionException_when_not_connected() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false

        // When
        useCase()
    }
}