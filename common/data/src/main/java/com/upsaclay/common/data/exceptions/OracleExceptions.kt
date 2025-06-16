package com.upsaclay.common.data.exceptions

import com.upsaclay.common.domain.entity.ConnectionClosedException
import com.upsaclay.common.domain.entity.DuplicateDataException
import com.upsaclay.common.domain.entity.InternalServerException

fun parseOracleException(code: String?, message: String?): Exception {
    return when (code) {
        "ORA-12801" -> DuplicateDataException(message)
        "ORA-03113" -> ConnectionClosedException(message)
        else -> InternalServerException(message)
    }
}