package com.elias.mismonedas.service

import android.os.Build
import android.util.Log
import com.elias.mismonedas.data.auth.TokenStore
import com.elias.mismonedas.data.repository.FCMTokenRepository
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class FinanxMessagingService : FirebaseMessagingService() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface FcmEntryPoint {
        fun fcmTokenRepository(): FCMTokenRepository
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken: ${token.take(12)}…")

        TokenStore.fcmToken = token

        val userId = TokenStore.userId
        if (userId > 0L) {
            val entryPoint = EntryPointAccessors.fromApplication(
                applicationContext, FcmEntryPoint::class.java
            )
            scope.launch {
                entryPoint.fcmTokenRepository().register(token, Build.MODEL)
            }
        }
    }

    override fun onMessageReceived(message: com.google.firebase.messaging.RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, "onMessageReceived from: ${message.from}")
    }

    companion object {
        private const val TAG = "FinanxFcm"
    }
}
