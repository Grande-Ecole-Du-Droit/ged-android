package com.upsaclay.common.domain.entity

class InternalServerException(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception()

class ServerCommunicationException(
    override val message: String? = null,
    override val cause: Throwable? = null,
): Exception()

class TooManyRequestException(
    override val message: String? = null,
    override val cause: Throwable? = null
): Exception()

class UserNotFoundException(
    override val message: String? = null,
    override val cause: Throwable? = null
): Exception()

class DuplicateDataException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()

class ForbiddenException(
    override val message: String? = null,
    override val cause: Throwable? = null
): Exception()