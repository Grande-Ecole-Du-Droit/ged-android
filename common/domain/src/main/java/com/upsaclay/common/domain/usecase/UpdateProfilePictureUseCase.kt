package com.upsaclay.common.domain.usecase

import android.net.Uri
import com.upsaclay.common.domain.UrlUtils.getFileNameFromUrl
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.withTimeout

class UpdateProfilePictureUseCase(
    private val fileRepository: FileRepository,
    private val imageRepository: ImageRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User, profilePictureUri: Uri) {
        val fileName = getFileName(user.id)
        val file = fileRepository.createFileFromUri(fileName, profilePictureUri)

        withTimeout(15000) {
            imageRepository.uploadImage(file)
            userRepository.updateProfilePictureFileName(user.id, file.name)
            user.profilePictureUrl?.let { deletePreviousProfilePicture(it) }
        }
    }

    private suspend fun deletePreviousProfilePicture(userProfilePictureUrl: String) {
        getFileNameFromUrl(userProfilePictureUrl)
            ?.let { imageRepository.deleteImage(it) }
    }

    private fun getFileName(userId: String) = "${userId}-profile-picture-${System.currentTimeMillis()}"
}