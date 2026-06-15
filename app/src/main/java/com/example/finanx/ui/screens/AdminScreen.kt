package com.example.finanx.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.LightBackground
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    authViewModel: AuthViewModel,
    onNavigateToCategories: () -> Unit = {},
    onNavigateToSavingGoals: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAnalyticsDashboard: () -> Unit = {}
) {
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                },
                actions = {
                    ProfileMenuAction(
                        onMyProfile = onNavigateToProfile,
                        onLogout = { authViewModel.logout(activity) }
                    )
                }
            )
        },
        containerColor = LightBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                AdminSection("Gestión de Usuarios") {
                    AdminButton("Registrar Usuario", true)
                    AdminButton("Bloquear Cuenta")
                    AdminButton("Habilitar Cuenta")
                    AdminButton("Deshabilitar Cuenta")
                }
            }

            item {
                AdminSection("Mantenimiento") {
                    AdminButton(
                        text = "Resumen financiero",
                        onClick = onNavigateToAnalyticsDashboard
                    )
                    AdminButton("Notificaciones", true)
                    AdminButton("Reportes")
                }
            }
        }
    }
}

@Composable
fun AdminSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 12.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun AdminButton(text: String, isPrimary: Boolean = false, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isPrimary) BluePrimary else Color.White,
            contentColor = if (isPrimary) Color.White else Color.Black
        ),
        elevation = ButtonDefaults.buttonElevation(1.dp)
    ) {
        Text(text)
    }
}
