package com.example.finanx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.zIndex

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpotlightOverlay(
    targetBounds: Rect?,
    message: String,
    isLastStep: Boolean,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    val scrimColor = Color(0xB3000000)

    if (targetBounds == null) {
        Box(modifier = Modifier.fillMaxSize().background(scrimColor), contentAlignment = androidx.compose.ui.Alignment.BottomCenter) {
            CaptionCard(message = message, isLastStep = isLastStep, onNext = onNext, onSkip = onSkip)
        }
        return
    }

    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenWidth = with(density) { windowInfo.containerSize.width.toDp() }
    val screenHeight = with(density) { windowInfo.containerSize.height.toDp() }

    val leftDp = with(density) { targetBounds.left.toDp() }
    val topDp = with(density) { targetBounds.top.toDp() }
    val rightDp = with(density) { targetBounds.right.toDp() }
    val bottomDp = with(density) { targetBounds.bottom.toDp() }
    val widthDp = (rightDp - leftDp).coerceAtLeast(1.dp)
    val heightDp = (bottomDp - topDp).coerceAtLeast(1.dp)
    val highlightColor = MaterialTheme.colorScheme.primary

    Box(modifier = Modifier.fillMaxSize()) {
        // Top scrim
        Box(
            modifier = Modifier
                .zIndex(3f)
                .fillMaxWidth()
                .height(topDp.coerceAtLeast(0.dp))
                .background(scrimColor)
        )

        // Bottom scrim
        Box(
            modifier = Modifier
                .zIndex(3f)
                .padding(top = bottomDp.coerceAtMost(screenHeight))
                .fillMaxWidth()
                .fillMaxHeight()
                .background(scrimColor)
        )

        // Left scrim
        Box(
            modifier = Modifier
                .zIndex(3f)
                .padding(top = topDp, bottom = (screenHeight - bottomDp).coerceAtLeast(0.dp))
                .width(leftDp.coerceAtLeast(0.dp))
                .fillMaxHeight()
                .background(scrimColor)
        )

        // Right scrim
        Box(
            modifier = Modifier
                .zIndex(3f)
                .padding(start = rightDp.coerceAtMost(screenWidth), top = topDp, bottom = (screenHeight - bottomDp).coerceAtLeast(0.dp))
                .fillMaxWidth()
                .fillMaxHeight()
                .background(scrimColor)
        )

        // Highlight border
        Box(
            modifier = Modifier
                .zIndex(4f)
                .padding(start = leftDp, top = topDp)
                .width(widthDp)
                .height(heightDp)
                .drawBehind {
                    drawRect(color = highlightColor, style = Stroke(width = 2.dp.toPx()))
                }
        )

        // Caption
        val captionTop = (bottomDp + 12.dp).coerceAtMost(screenHeight - 136.dp)
        Box(
            modifier = Modifier
                .zIndex(5f)
                .padding(start = 16.dp, end = 16.dp, top = captionTop)
                .fillMaxWidth()
        ) {
            CaptionCard(message = message, isLastStep = isLastStep, onNext = onNext, onSkip = onSkip)
        }
    }
}

@Composable
private fun CaptionCard(
    message: String,
    isLastStep: Boolean,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.widthIn(max = 520.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Button(onClick = onSkip) { Text("Saltar tutorial") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNext) { Text(if (isLastStep) "Entendido" else "Siguiente") }
            }
        }
    }
}
