package com.upsaclay.common.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.upsaclay.common.data.exceptions.mapNetworkException
import com.upsaclay.common.data.remote.api.ImageApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

internal class ImageRemoteDataSource(private val imageApi: ImageApi) {
    suspend fun getImage(fileName: String): Bitmap? = withContext(Dispatchers.IO) {
        mapNetworkException(
            block = { imageApi.getImage(fileName) }
        ).body?.byteStream()?.let(BitmapFactory::decodeStream)
    }

    suspend fun uploadImage(file: File) {
        withContext(Dispatchers.IO) {
            val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
            mapNetworkException(
                block = { imageApi.uploadImage(multipartBody) }
            )
        }
    }

    suspend fun deleteImage(fileName: String) {
        withContext(Dispatchers.IO) {
           mapNetworkException(
               block = { imageApi.deleteImage(fileName) }
           )
        }
    }
}