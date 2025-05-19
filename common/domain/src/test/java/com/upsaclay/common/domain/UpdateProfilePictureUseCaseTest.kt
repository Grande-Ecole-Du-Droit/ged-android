package com.upsaclay.common.domain

import android.net.Uri
import com.upsaclay.common.domain.repository.DrawableRepository
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

class UpdateProfilePictureUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val imageRepository: ImageRepository = mockk()
    private val drawableRepository: DrawableRepository = mockk()
    private val fileRepository: FileRepository = mockk()

    private lateinit var updateProfilePictureUseCase: UpdateProfilePictureUseCase

    private val uri: Uri = mockk()
    private val file: File = File("file")

    @Before
    fun setUp() {
        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { drawableRepository.getDrawableUri(any()) } returns uri
        every { fileRepository.getFileType(any()) } returns "jpg"
        coEvery { imageRepository.uploadImage(any()) } returns Unit
        coEvery { imageRepository.deleteImage(any()) } returns Unit
        coEvery { userRepository.updateProfilePictureFileName(any(), any()) } returns Unit
        coEvery { fileRepository.createFileFromUri(any(), any()) } returns file
        coEvery { fileRepository.createFileFromByteArray(any(), any()) } returns file

        updateProfilePictureUseCase = UpdateProfilePictureUseCase(
            fileRepository = fileRepository,
            imageRepository = imageRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun updateProfilePictureUseCase_should_update_profile_picture() = runTest {
        // When
        updateProfilePictureUseCase(userFixture, uri)

        // Then
        coVerify { userRepository.updateProfilePictureFileName(userFixture.id, any()) }
        coVerify { imageRepository.uploadImage(any()) }
    }

    @Test
    fun updateProfilePictureUseCase_should_delete_previous_profile_picture() = runTest {
        // When
        updateProfilePictureUseCase(userFixture, uri)

        // Then
        coVerify { userRepository.updateProfilePictureFileName(userFixture.id, any()) }
        coVerify { imageRepository.deleteImage(userFixture.profilePictureFileName!!.substringAfterLast("/")) }
    }
}