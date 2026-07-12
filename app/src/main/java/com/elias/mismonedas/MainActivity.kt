package com.elias.mismonedas

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.elias.mismonedas.data.auth.TokenStore
import com.elias.mismonedas.data.repository.FCMTokenRepository
import com.elias.mismonedas.ui.MainScreen
import com.elias.mismonedas.ui.theme.FinanxTheme
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var fcmTokenRepository: FCMTokenRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerFcmTokenIfNeeded()
        setContent {
            FinanxTheme {
                MainScreen()
            }
        }
    }

    private fun registerFcmTokenIfNeeded() {
        scope.launch {
            try {
                val token = FirebaseMessaging.getInstance().token.await()
                val lastToken = TokenStore.fcmToken
                if (token != lastToken) {
                    TokenStore.fcmToken = token
                    val userId = TokenStore.userId
                    if (userId > 0L) {
                        fcmTokenRepository.register(userId, token, Build.MODEL)
                        Log.d(TAG, "FCM token registered for user $userId")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get FCM token", e)
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}