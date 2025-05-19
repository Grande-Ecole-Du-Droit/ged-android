package com.upsaclay.common.data.exceptions

import com.upsaclay.common.domain.entity.InternalServerException

class DataIntegrityViolationException(
    override val message: String? = null,
    override val cause: Throwable? = null
): Exception()

fun parseOracleException(code: String?, message: String?): Exception {
    return when (code) {
        "ORA-12801" -> DataIntegrityViolationException()
        else -> InternalServerException(message)
    }
}