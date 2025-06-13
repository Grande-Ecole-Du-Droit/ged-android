package com.upsaclay.authentication.domain.entity.exception

class InvalidCredentialsException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()

class UserDisabledException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()