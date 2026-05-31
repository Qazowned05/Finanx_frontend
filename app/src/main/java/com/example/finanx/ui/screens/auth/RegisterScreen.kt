package com.example.finanx.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.LightBackground
import com.example.finanx.ui.viewmodel.AuthState
import com.example.finanx.ui.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(viewModel: AuthViewModel, onBackToLogin: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val state = viewModel.authState

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LightBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Crear Cuenta",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BluePrimary
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (state is AuthState.Loading) {
                CircularProgressIndicator(color = BluePrimary)
            } else {
                Button(
                    onClick = { viewModel.register(name, email, password) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                ) {
                    Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                TextButton(onClick = onBackToLogin) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }

            if (state is AuthState.Error) {
                Text(text = state.message, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
