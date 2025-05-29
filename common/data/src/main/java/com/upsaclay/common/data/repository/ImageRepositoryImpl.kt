package com.upsaclay.common.data.repository

import android.graphics.Bitmap
import com.upsaclay.common.data.remote.ImageRemoteDataSource
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.ImageRepository
import java.io.File

internal class ImageRepositoryImpl(
    private val imageRemoteDataSource: ImageRemoteDataSource
): ImageRepository {
    override suspend fun getImage(fileName: String): Bitmap? {
        return try {
            imageRemoteDataSource.getImage(fileName)
        } catch (e: Exception) {
            e("Failed to get image: ${e.message}", e)
            throw e
        }
    }

    override suspend fun uploadImage(file: File) {
        try {
            imageRemoteDataSource.uploadImage(file)
        } catch (e: Exception) {
            e("Failed to upload image: ${e.message}", e)
            throw e
        }
    }

    override suspend fun deleteImage(fileName: String) {
        try {
            imageRemoteDataSource.deleteImage(fileName)
        } catch (e: Exception) {
            e("Failed to delete image: ${e.message}", e)
            throw e
        }
    }
}