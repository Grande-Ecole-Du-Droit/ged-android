package com.upsaclay.news.domain

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.UpdateAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: UpdateAnnouncementUseCase

    @Before
    fun setUp() {
        every { connectivityObserver.isConnected } returns true
        coEvery { announcementRepository.updateAnnouncement(any()) } returns Unit

        useCase = UpdateAnnouncementUseCase(
            announcementRepository = announcementRepository,
            connectivityObserver = connectivityObserver
        )
    }

    @Test
    fun updateAnnouncement_should_update_announcement() = runTest {
        // When
        useCase(announcementFixture)

        // Then
        coVerify { announcementRepository.updateAnnouncement(announcementFixture) }
    }

    @Test(expected = NoInternetConnectionException::class)
    fun updateAnnouncement_should_throw_NoInternetConnectionException_when_not_connected() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false

        // When
        useCase(announcementFixture)
    }
}