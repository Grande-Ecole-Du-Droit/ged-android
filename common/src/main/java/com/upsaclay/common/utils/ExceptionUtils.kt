package com.upsaclay.common.utils

import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.InternalServerException
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import okio.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun mapNetworkErrorMessage(
    error: Throwable,
    specificMap: (Throwable) -> Int = { R.string.unknown_error }
) : Int {
    return when(error) {
        is NoInternetConnectionException, is UnknownHostException -> R.string.no_internet_connection
        is ConnectException -> R.string.server_connection_error
        is SocketTimeoutException -> R.string.timeout_error
        is InternalServerException -> R.string.internal_server_error
        is IOException -> R.string.unknown_network_error
        else -> specificMap(error)
    }
}