package com.example.finanx.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.theme.BluePrimary
import com.example.finanx.ui.theme.BlueSecondary
import com.example.finanx.ui.theme.LightBackground
import com.example.finanx.ui.theme.TextGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Finanzas", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Person, contentDescription = null)
                    }
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
            item {
                Text(
                    text = "Hola, Juan",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = BluePrimary
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Saldo Actual", color = TextGray)
                        Text(
                            "PEN 8,200.00",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = BluePrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .background(Color(0xFFEDF2F7), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Gráfico de balance", color = TextGray, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SummaryCard("Ingresos", "PEN 5,700.00", Modifier.weight(1f))
                    SummaryCard("Gastos", "PEN 3,200.00", Modifier.weight(1f))
                }
            }

            item {
                Text("Proyección", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Proyección Financiera", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(16.dp))
                        // Placeholder for big chart
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                                .background(Color(0xFFF7FAFC), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Gráfico de Líneas (Ingresos vs Gastos)", color = Color.Gray)
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Text("+ Nueva Transacción")
                    }
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BluePrimary)
                    ) {
                        Text("Ver Reporte")
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(title: String, amount: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontSize = 12.sp, color = TextGray)
            Text(amount, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = BlueSecondary)
        }
    }
}
