package com.example.finanx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.BlueSecondary
import com.example.finanx.ui.theme.LightBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Análisis", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Person, contentDescription = null) }
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Text("Insights", fontWeight = FontWeight.Bold, fontSize = 18.sp) }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InsightItem("Resumen", Icons.Default.Description, Color(0xFF63B3ED))
                    InsightItem("Gastos por Categoría", Icons.Default.PieChart, Color(0xFFFC8181))
                    InsightItem("Tendencias", Icons.Default.TrendingUp, Color(0xFF68D391))
                }
            }

            item { Text("Notificaciones", fontWeight = FontWeight.Bold, fontSize = 18.sp) }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    NotificationItem("Pago de Luz pendiente", Color(0xFFF6AD55))
                    NotificationItem("Nueva Actualización Disponible", Color(0xFFF6AD55))
                    NotificationItem("Cuenta bloqueada (Admin)", Color(0xFFF6AD55))
                }
            }

            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    OutlinedButton(onClick = {}) {
                        Text("Ver Todos")
                    }
                }
            }
        }
    }
}

@Composable
fun InsightItem(title: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color)
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun NotificationItem(message: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Box(modifier = Modifier.size(10.dp).background(color, RoundedCornerShape(2.dp)))
        Spacer(modifier = Modifier.width(12.dp))
        Text(message, fontSize = 14.sp)
    }
}
