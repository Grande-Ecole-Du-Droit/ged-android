package com.upsaclay.gedoise.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.upsaclay.authentication.AuthenticationBaseRoute
import com.upsaclay.common.domain.entity.FcmDataType
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.gedoise.presentation.navigation.GedNavHost
import com.upsaclay.gedoise.presentation.navigation.SplashScreenRoute
import com.upsaclay.gedoise.presentation.viewmodels.MainViewModel
import com.upsaclay.gedoise.presentation.viewmodels.NavigationViewModel
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.news.presentation.NewsBaseRoute
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("MissingPermission")
class MainActivity : ComponentActivity() {
    companion object {
        const val CONVERSATION_ID_EXTRA = "conversation_id_extra"
    }
    private val mainViewModel: MainViewModel by viewModel()
    private val navigationViewModel: NavigationViewModel by viewModel()
    private val notificationPresenter: NotificationPresenter by inject<NotificationPresenter>()
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                notificationPresenter.start()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.startListening()
        startNotification()

        val splashscreen = installSplashScreen()
        splashscreen.setKeepOnScreenCondition {
            navigationViewModel.uiState.value.startDestination is SplashScreenRoute
        }

        setContent {
            GedoiseTheme {
                GedNavHost(navigationViewModel = navigationViewModel)
            }
        }

        intent?.let {
            handleIntent(it)
        }
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                notificationPresenter.start()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            notificationPresenter.start()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val conversationJson = intent.getStringExtra(CONVERSATION_ID_EXTRA)
        val notificationType = intent.getStringExtra("type")

        when {
            conversationJson != null ->
                navigationViewModel.intentToNavigate(ChatRoute(conversationJson))

            notificationType != null ->
                handleNotificationIntent(notificationType, intent.extras)
        }
    }

    private fun handleNotificationIntent(type: String, extras: Bundle?) {
        when (type) {
            FcmDataType.MESSAGE.toString() -> {
                extras?.getString("value")?.let { conversation ->
                    navigationViewModel.intentToNavigate(ChatRoute(conversation))
                }
            }

            else -> Unit
        }
    }
}