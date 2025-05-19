package com.upsaclay.news

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RecreateAnnouncementUseCase
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import com.upsaclay.news.presentation.news.NewsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {
    private val recreateAnnouncementUseCase: RecreateAnnouncementUseCase = mockk()
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase = mockk()
    private val refreshAnnouncementUseCase: RefreshAnnouncementUseCase = mockk()

    private val userRepository: UserRepository = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()

    private lateinit var newsViewModel: NewsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { announcementRepository.announcements } returns flowOf(announcementsFixture)
        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { recreateAnnouncementUseCase(any()) } returns Unit
        coEvery { refreshAnnouncementUseCase() } returns Unit
        coEvery { refreshAnnouncementUseCase.refreshing } returns MutableStateFlow(false)
        coEvery { deleteAnnouncementUseCase(any()) } returns Unit

        newsViewModel = NewsViewModel(
            recreateAnnouncementUseCase = recreateAnnouncementUseCase,
            deleteAnnouncementUseCase = deleteAnnouncementUseCase,
            refreshAnnouncementUseCase = refreshAnnouncementUseCase,
            announcementRepository = announcementRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun refreshAnnouncements_should_refresh_announcements() = runTest {
        // When
        newsViewModel.refreshAnnouncements()

        // Then
        coVerify { refreshAnnouncementUseCase() }
    }

    @Test
    fun recreateAnnouncement_should_resend_announcement() = runTest {
        // Given
        val announcement = announcementsFixture.first()

        // When
        newsViewModel.recreateAnnouncement(announcement)

        // Then
        coVerify { recreateAnnouncementUseCase(announcement) }
    }

    @Test
    fun deleteAnnouncement_should_delete_announcement() = runTest {
        // Given
        val announcement = announcementsFixture.first()

        // When
        newsViewModel.deleteAnnouncement(announcement)

        // Then
        coVerify { deleteAnnouncementUseCase(announcement) }
    }
}