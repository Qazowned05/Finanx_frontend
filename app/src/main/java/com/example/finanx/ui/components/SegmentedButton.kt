package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SegmentedButton(items: List<String>, onItemSelected: (Int) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }

    Row(modifier = Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            Button(
                onClick = { 
                    selectedIndex = index
                    onItemSelected(index) 
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(item)
            }
        }
    }
}
