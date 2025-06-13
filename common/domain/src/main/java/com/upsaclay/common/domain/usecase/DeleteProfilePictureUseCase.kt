package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository

class DeleteProfilePictureUseCase(
    private val userRepository: UserRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(userId: String, profilePictureUrl: String) {
        userRepository.deleteProfilePictureFileName(userId)
        imageRepository.deleteImage(profilePictureUrl.substringAfterLast("/"))
    }
}