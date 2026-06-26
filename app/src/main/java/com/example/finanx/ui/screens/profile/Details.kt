package com.example.finanx.ui.screens.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finanx.ui.screens.saving.DetailRow
import com.example.finanx.ui.theme.BlueSecondary
import com.example.finanx.ui.theme.GrayBorder
import com.example.finanx.ui.theme.TextDark
import com.example.finanx.ui.theme.TextGray
import com.example.finanx.ui.util.toReadableDate
import org.openapitools.client.models.UserResponse


@Composable
fun AccountDetailsSection(
    user: UserResponse,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Surface(
                onClick = onToggle,
                shape = RoundedCornerShape(12.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = BlueSecondary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Ver otros datos", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextDark, modifier = Modifier.weight(1f))
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextGray
                    )
                }
            }
            if (expanded) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = GrayBorder)
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    DetailRow("Usuario", user.username ?: "-")
                    DetailRow("Zona Horaria", user.timeZone?.value?.replace("_", " ")?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "-")
                    DetailRow("Creado", user.createdAt?.toLocalDate().toReadableDate())
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 14.sp, color = TextGray)
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextDark)
    }
}