package com.upsaclay.common.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.api.ImageApi
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.InternalServerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

internal class ImageRemoteDataSource(
    private val imageApi: ImageApi
) {
    suspend fun getImage(fileName: String): Bitmap? = withContext(Dispatchers.IO) {
        val response = imageApi.getImage(fileName)
        if (response.isSuccessful) {
            response.body?.byteStream()?.let(BitmapFactory::decodeStream)
        } else {
            e(formatHttpError("Error getting image:", response))
            throw InternalServerException()
        }
    }

    suspend fun uploadImage(file: File) {
        withContext(Dispatchers.IO) {
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
            val response = imageApi.uploadImage(multipartBody)
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError("Error uploading image", response)
                throw InternalServerException(errorMessage)
            }
        }
    }

    suspend fun deleteImage(imageName: String) {
        withContext(Dispatchers.IO) {
            val response = imageApi.deleteImage(imageName)
            if (!response.isSuccessful) {
                val errorMessage = formatHttpError("Error deleting image", response)
                throw IOException(errorMessage)
            }
        }
    }
}