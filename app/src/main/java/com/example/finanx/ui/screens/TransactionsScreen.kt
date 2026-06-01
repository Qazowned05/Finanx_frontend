package com.example.finanx.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.theme.*
import com.example.finanx.ui.components.ProfileMenuAction
import com.example.finanx.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(authViewModel: AuthViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val activity = context as? Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transacciones", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) { Icon(Icons.Default.ArrowBack, contentDescription = null) }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Search, contentDescription = null) }
                    IconButton(onClick = {}) { Icon(Icons.Default.History, contentDescription = null) }
                    ProfileMenuAction(
                        onMyProfile = { /* TODO: navegar a perfil */ },
                        onLogout = { authViewModel.logout(activity) }
                    )
                }
            )
        },
        containerColor = LightBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabButton("Todas", selectedTab == 0, Modifier.weight(1f)) { selectedTab = 0 }
                TabButton("Programadas", selectedTab == 1, Modifier.weight(1f)) { selectedTab = 1 }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(dummyTransactions) { transaction ->
                    TransactionItem(transaction)
                }
                
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary)
                    ) {
                        Text("+ Nueva Categoría")
                    }
                }
            }
        }
    }
}

@Composable
fun TabButton(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) BluePrimary else Color.White,
            contentColor = if (isSelected) Color.White else BluePrimary
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = ButtonDefaults.buttonElevation(if (isSelected) 2.dp else 0.dp)
    ) {
        Text(text)
    }
}

@Composable
fun TransactionItem(transaction: TransactionData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(transaction.iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(transaction.icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(transaction.title, fontWeight = FontWeight.Bold, color = TextDark)
                if (transaction.isProgrammed) {
                    Text("Programada", color = BlueSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
            Text(
                "${if (transaction.isIncome) "" else "- "}PEN ${transaction.amount}",
                fontWeight = FontWeight.ExtraBold,
                color = if (transaction.isIncome) IncomeGreen else TextDark
            )
        }
    }
}

data class TransactionData(
    val title: String,
    val amount: String,
    val isIncome: Boolean,
    val isProgrammed: Boolean,
    val icon: ImageVector,
    val iconBg: Color
)

val dummyTransactions = listOf(
    TransactionData("Supermercado", "250.00", false, false, Icons.Default.ShoppingCart, Color(0xFFF6AD55)),
    TransactionData("Sueldo", "1,500.00", true, false, Icons.Default.Payments, Color(0xFF48BB78)),
    TransactionData("Netflix", "45.00", false, false, Icons.Default.PlayArrow, Color(0xFFE53E3E)),
    TransactionData("Seguro Vehicular", "250.00", false, true, Icons.Default.DirectionsCar, Color(0xFF3182CE))
)
