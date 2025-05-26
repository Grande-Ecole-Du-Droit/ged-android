package com.upsaclay.common.data.exceptions

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.DuplicateDataException
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.TooManyRequestException
import java.net.UnknownHostException

suspend fun <T> Any.handleNetworkException(
    block: suspend () -> T,
    message: String? = null,
    mapSpecificException: (Exception) -> Exception = { it }
): T {
    return try {
        block()
    } catch (e: FirebaseNetworkException) {
        this.e("$message: ${e.message}", e)
        throw UnknownHostException()
    } catch (e: FirebaseTooManyRequestsException) {
        e("$message: ${e.message}", e)
        throw TooManyRequestException()
    } catch (e: DataIntegrityViolationException) {
        e("$message: ${e.message}", e)
        throw DuplicateDataException()
    } catch (e: Exception) {
        e("$message: ${e.message}", e)
        throw mapSpecificException(e)
    }
}