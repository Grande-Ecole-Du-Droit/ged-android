package com.upsaclay.common.data.exceptions

import android.accounts.NetworkErrorException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.DuplicateDataException
import com.upsaclay.common.domain.entity.TooManyRequestException

suspend fun <T> Any.handleNetworkException(
    block: suspend () -> T,
    message: String? = null,
    catchSpecificException: (Exception) -> T = {
        e(message ?: it.message.toString(), it)
        throw it
    }
): T {
    return try {
        block()
    } catch (e: FirebaseNetworkException) {
        this.e("$message: ${e.message}", e)
        throw NetworkErrorException()
    } catch (e: FirebaseTooManyRequestsException) {
        e("$message: ${e.message}", e)
        throw TooManyRequestException()
    } catch (e: DataIntegrityViolationException) {
        e("$message: ${e.message}", e)
        throw DuplicateDataException()
    } catch (e: FirebaseAuthException) {
        e("$message: ${e.message}", e)
        catchSpecificException(e)
    } catch (e: Exception) {
        e("$message: ${e.message}", e)
        throw e
    }
}