package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class IsUserAuthenticatedUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    operator fun invoke(): Flow<Boolean> = authenticationRepository.isAuthenticated
}