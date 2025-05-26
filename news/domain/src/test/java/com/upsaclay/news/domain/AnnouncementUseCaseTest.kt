package com.upsaclay.news.domain

import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AnnouncementUseCaseTest {
    private lateinit var createAnnouncementUseCase: CreateAnnouncementUseCase
    private lateinit var deleteAnnouncementUseCase: DeleteAnnouncementUseCase

    private val announcementRepository: AnnouncementRepository = mockk()

    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        createAnnouncementUseCase = CreateAnnouncementUseCase(
            announcementRepository = announcementRepository,
            scope = testScope
        )
        deleteAnnouncementUseCase = DeleteAnnouncementUseCase(
            announcementRepository = announcementRepository
        )

        coEvery { announcementRepository.createLocalAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.deleteAnnouncement(any()) } returns Unit
    }

    @Test
    fun create_announcement_use_case_should_create_announcement() = runTest {
        // When
        createAnnouncementUseCase(announcementFixture)

        // Then
        coVerify { announcementRepository.createLocalAnnouncement(announcementFixture) }
    }

    @Test
    fun delete_announcement_use_case_should_delete_announcement() = runTest {
        // When
        deleteAnnouncementUseCase(announcementFixture)

        // Then
        coVerify { announcementRepository.deleteAnnouncement(announcementFixture) }
    }
}