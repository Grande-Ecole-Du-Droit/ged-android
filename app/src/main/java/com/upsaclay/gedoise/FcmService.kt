package com.upsaclay.gedoise

import android.content.Intent
import android.os.Bundle
import com.google.firebase.messaging.FirebaseMessagingService
import com.upsaclay.common.domain.entity.FcmDataType
import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.gedoise.domain.usecase.FcmTokenUseCase
import com.upsaclay.message.domain.MessageJsonConverter
import com.upsaclay.message.presentation.MessageNotificationPresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FcmService: FirebaseMessagingService() {
    private var job: Job? = null
    private val messageNotificationPresenter: MessageNotificationPresenter by inject<MessageNotificationPresenter>()
    private val fcmTokenUseCase: FcmTokenUseCase by inject<FcmTokenUseCase>()
    private val userRepository: UserRepository by inject<UserRepository>()
    private val scope = CoroutineScope(SupervisorJob())

    override fun onNewToken(tokenValue: String) {
        super.onNewToken(tokenValue)
        job?.cancel()
        job = scope.launch(Dispatchers.IO) {
            userRepository.user
                .filterNotNull()
                .take(1)
                .collect {
                    fcmTokenUseCase.sendFcmToken(FcmToken(it.id, tokenValue))
                }
        }
    }

    override fun handleIntent(intent: Intent?) {
        super.handleIntent(intent)
        scope.launch(Dispatchers.Main) {
            when (intent?.extras?.getString("type")) {
                FcmDataType.MESSAGE.toString() -> showMessageNotification(intent.extras)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private suspend fun showMessageNotification(extra: Bundle?) {
        extra?.getString("value")?.let { value ->
            MessageJsonConverter.toConversationMessage(value)?.let  {
                messageNotificationPresenter.showNotification(it)
            }
        }
    }
}