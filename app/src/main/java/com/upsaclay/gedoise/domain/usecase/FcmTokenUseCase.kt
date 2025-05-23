package com.upsaclay.gedoise.domain.usecase

import com.google.firebase.messaging.FirebaseMessaging
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.repository.CredentialsRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FcmTokenUseCase(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val credentialsRepository: CredentialsRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val scope: CoroutineScope
) {
    fun listenEvents() {
        scope.launch {
           combine(
               authenticationRepository.isAuthenticated.filterNotNull(),
               connectivityObserver.isConnected.filter { it }
           ) { isAuthenticated, _ ->
                isAuthenticated
           }
               .collectLatest { isAuthenticated ->
                   when {
                       isAuthenticated -> {
                           credentialsRepository.getUnsentFcmToken()?.let { fcmToken ->
                               val userId = fcmToken.userId ?: userRepository.user.filterNotNull().first().id
                               sendFcmToken(fcmToken.copy(userId = userId))
                           }
                       }

                       !isAuthenticated -> {
                           credentialsRepository.removeUnsentFcmToken()
                           FirebaseMessaging.getInstance().deleteToken()
                           val token = FirebaseMessaging.getInstance().token.await()
                           credentialsRepository.storeUnsentFcmToken(FcmToken(null, token))
                       }
                   }
               }
           }
    }

    suspend fun sendFcmToken(fcmToken: FcmToken) {
        runCatching {
            credentialsRepository.sendFcmToken(fcmToken)
            credentialsRepository.removeUnsentFcmToken()
        }
            .onFailure {
                credentialsRepository.storeUnsentFcmToken(fcmToken)
            }
    }

    suspend fun storeToken(fcmToken: FcmToken) {
        credentialsRepository.storeUnsentFcmToken(fcmToken)
    }
}