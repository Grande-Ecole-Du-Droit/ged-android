package com.upsaclay.gedoise

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.upsaclay.common.domain.entity.FcmDataType
import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.gedoise.domain.usecase.FcmTokenUseCase
import com.upsaclay.gedoise.presentation.NotificationPresenter
import com.upsaclay.message.domain.MessageJsonConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FcmService: FirebaseMessagingService() {
    private var job: Job? = null
    private val notificationPresenter: NotificationPresenter by inject<NotificationPresenter>()
    private val fcmTokenUseCase: FcmTokenUseCase by inject<FcmTokenUseCase>()
    private val userRepository: UserRepository by inject<UserRepository>()
    private val scope = GlobalScope

    override fun onNewToken(tokenValue: String) {
        super.onNewToken(tokenValue)
        job?.cancel()
        job = scope.launch(Dispatchers.IO) {
            userRepository.currentUser?.let {
                fcmTokenUseCase.sendFcmToken(FcmToken(it.id, tokenValue))
            } ?: run {
                fcmTokenUseCase.storeToken(FcmToken(null, tokenValue))
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        scope.launch(Dispatchers.Main) {
           when(remoteMessage.data["type"]) {
               FcmDataType.MESSAGE.toString() -> handleNotification(remoteMessage)
           }
        }
    }

    private suspend fun handleNotification(remoteMessage: RemoteMessage) {
        remoteMessage.data["value"]?.let { value ->
            MessageJsonConverter.fromConversationMessage(value)?.let {
                notificationPresenter.showMessageNotification(it)
            }
        }
    }
}