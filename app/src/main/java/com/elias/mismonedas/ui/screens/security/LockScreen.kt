package com.elias.mismonedas.ui.screens.security

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.elias.mismonedas.data.auth.SecurityStore

@Composable
fun LockScreen(
    onUnlock: () -> Unit,
    onLogout: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var shake by remember { mutableStateOf(false) }
    val shakeOffset by animateFloatAsState(
        targetValue = if (shake) 10f else 0f,
        animationSpec = spring(stiffness = 300f),
        label = "shake"
    )

    LaunchedEffect(shake) {
        if (shake) {
            kotlinx.coroutines.delay(500)
            shake = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Bloqueo de seguridad",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Ingresa tu PIN para desbloquear",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = pin,
            onValueChange = {
                if (it.length <= 6) {
                    pin = it
                    error = null
                }
            },
            label = { Text("PIN") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            isError = error != null,
            supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier
                .fillMaxWidth()
                .scale(1f + (shakeOffset / 100f))
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (SecurityStore.verifyPin(pin)) {
                    SecurityStore.failedAttempts = 0
                    onUnlock()
                } else {
                    SecurityStore.failedAttempts++
                    SecurityStore.lastFailedAttempt = System.currentTimeMillis()
                    val attempts = SecurityStore.failedAttempts
                    if (attempts >= 3) {
                        SecurityStore.clearPin()
                        onLogout()
                    } else {
                        error = "PIN incorrecto. Intento ${attempts}/3"
                        shake = true
                        pin = ""
                    }
                }
            },
            enabled = pin.length >= 4,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Desbloquear", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))

        TextButton(onClick = {
            SecurityStore.clearPin()
            onLogout()
        }) {
            Text("Olvidé mi PIN")
        }
    }
}
