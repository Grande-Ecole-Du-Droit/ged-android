package com.upsaclay.news

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.presentation.announcement.read.ReadAnnouncementViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReadAnnouncementViewModelTest {
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase = mockk()

    private val userRepository: UserRepository = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()

    private lateinit var readAnnouncementViewModel: ReadAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(announcementFixture.author)
        every { announcementRepository.getAnnouncement(announcementFixture.id) } returns announcementFixture
        every { announcementRepository.getAnnouncementFlow(announcementFixture.id) } returns flowOf(announcementFixture)
        coEvery { deleteAnnouncementUseCase(announcementFixture) } returns Unit

        readAnnouncementViewModel = ReadAnnouncementViewModel(
            announcementId = announcementFixture.id,
            deleteAnnouncementUseCase = deleteAnnouncementUseCase,
            userRepository = userRepository,
            announcementRepository = announcementRepository
        )
    }

    @Test
    fun deleteAnnouncement_should_not_delete_announcement_when_announcement_is_null() {
        // Given
        every { announcementRepository.getAnnouncement(announcementFixture.id) } returns null

        readAnnouncementViewModel = ReadAnnouncementViewModel(
            announcementId = announcementFixture.id,
            userRepository = userRepository,
            deleteAnnouncementUseCase = deleteAnnouncementUseCase,
            announcementRepository = announcementRepository
        )

        // When
        readAnnouncementViewModel.deleteAnnouncement()

        // Then
        coVerify(exactly = 0) { deleteAnnouncementUseCase(announcementFixture) }
    }
}