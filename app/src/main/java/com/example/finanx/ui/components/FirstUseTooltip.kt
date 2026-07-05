package com.example.finanx.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.foundation.layout.Arrangement

@Composable
fun FirstUseTooltip(
    targetBounds: Rect?,
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (targetBounds == null) return

    Popup(
        alignment = Alignment.TopStart,
        offset = IntOffset(
            x = targetBounds.left.toInt(),
            y = (targetBounds.bottom + 12f).toInt()
        ),
        properties = PopupProperties(focusable = false)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.inverseOnSurface,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            modifier = modifier.widthIn(max = 280.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(message)
                Row(horizontalArrangement = Arrangement.End) {
                    Button(onClick = onDismiss) {
                        Text("Entendido")
                    }
                }
            }
        }
    }
}
