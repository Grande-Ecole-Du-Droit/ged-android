package com.upsaclay.common.domain

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.DeleteProfilePictureUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteProfilePictureUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var deleteProfilePictureUseCase: DeleteProfilePictureUseCase

    @Before
    fun setUp() {
        coEvery { userRepository.deleteProfilePictureFileName(any()) } returns Unit
        coEvery { imageRepository.deleteImage(any()) } returns Unit

        deleteProfilePictureUseCase = DeleteProfilePictureUseCase(
            userRepository = userRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun deleteProfilePictureUseCase_should_delete_image() = runTest {
        // Given
        val url = "url"

        // When
        deleteProfilePictureUseCase(userFixture.id, url)

        // Then
        coVerify { userRepository.deleteProfilePictureFileName(userFixture.id) }
        coVerify { imageRepository.deleteImage(url.substringAfterLast("/")) }
    }
}