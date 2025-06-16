package com.upsaclay.common.data.exceptions

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.common.domain.entity.TooManyRequestException
import retrofit2.Response
import java.net.UnknownHostException

suspend fun <T> Any.mapFirebaseException(
    block: suspend () -> T,
    message: String? = null,
    specificMap: (Exception) -> Exception = { it }
): T {
    return try {
        block()
    } catch (e: FirebaseNetworkException) {
        this.e("$message: ${e.message}", e)
        throw UnknownHostException()
    } catch (e: FirebaseTooManyRequestsException) {
        e("$message: ${e.message}", e)
        throw TooManyRequestException()
    } catch (e: Exception) {
        e("$message: ${e.message}", e)
        throw specificMap(e)
    }
}

suspend fun <T> mapServerResponseException(
    block: suspend () -> Response<T>,
    specificMap: ((Response<T>) -> T?)? = null
): T? {
    val response = block()
    return if (response.isSuccessful) {
        response.body()
    } else {
        specificMap?.let {
            it(response)
        } ?: run {
            val errorMessage = formatHttpError(response)
            throw InternalServerException(errorMessage)
        }
    }
}