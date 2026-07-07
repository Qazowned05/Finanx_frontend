package com.example.finanx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

@Composable
fun SpotlightOverlay(
    targetBounds: Rect?,
    message: String,
    isLastStep: Boolean,
    showNextButton: Boolean = true,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    val scrimColor = Color(0xB3000000)
    val highlightColor = MaterialTheme.colorScheme.primary

    if (targetBounds == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(scrimColor)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent().changes.forEach { it.consume() }
                        }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            OverviewCard(
                message = message,
                isLastStep = isLastStep,
                showNextButton = showNextButton,
                onNext = onNext,
                onSkip = onSkip
            )
        }
        return
    }

    val density = LocalDensity.current
    val windowInfo = LocalWindowInfo.current
    val screenHeightDp = with(density) { windowInfo.containerSize.height.toDp() }
    val cutoutRadiusPx = with(density) { 12.dp.toPx() }
    val borderWidthPx = with(density) { 2.dp.toPx() }

    var overlayOffset by remember { mutableStateOf(Offset.Zero) }
    var captionHeightDp by remember { mutableStateOf(120.dp) }

    val offset = overlayOffset
    val localRect = targetBounds.translate(-offset.x, -offset.y)
    val targetTopDp = with(density) { localRect.top.toDp() }
    val targetBottomDp = with(density) { localRect.bottom.toDp() }
    val targetCenterYDp = (targetTopDp + targetBottomDp) / 2

    val captionTopDp = if (targetCenterYDp > screenHeightDp / 2) {
        (targetTopDp - captionHeightDp - 12.dp).coerceAtLeast(16.dp)
    } else {
        (targetBottomDp + 12.dp).coerceAtMost(screenHeightDp - captionHeightDp - 16.dp)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { overlayOffset = it.boundsInRoot().topLeft }
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent().changes.forEach { it.consume() }
                        }
                    }
                }
                .drawBehind {
                    val localRect = targetBounds.translate(-overlayOffset.x, -overlayOffset.y)
                    val path = Path().apply {
                        addRect(Rect(Offset.Zero, size))
                        addRoundRect(
                            RoundRect(
                                rect = localRect,
                                cornerRadius = CornerRadius(cutoutRadiusPx)
                            )
                        )
                        fillType = PathFillType.EvenOdd
                    }
                    drawPath(path, scrimColor)
                    drawRect(
                        color = highlightColor,
                        topLeft = Offset(localRect.left, localRect.top),
                        size = Size(localRect.width, localRect.height),
                        style = Stroke(width = borderWidthPx)
                    )
                }
        )

        Box(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = captionTopDp)
                .fillMaxWidth()
                .onSizeChanged { captionHeightDp = with(density) { it.height.toDp() } }
        ) {
            CaptionCard(
                message = message,
                isLastStep = isLastStep,
                showNextButton = showNextButton,
                onNext = onNext,
                onSkip = onSkip
            )
        }
    }
}

@Composable
private fun CaptionCard(
    message: String,
    isLastStep: Boolean,
    showNextButton: Boolean = true,
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
                if (showNextButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onNext) { Text(if (isLastStep) "Entendido" else "Siguiente") }
                }
            }
        }
    }
}

@Composable
private fun OverviewCard(
    message: String,
    isLastStep: Boolean,
    showNextButton: Boolean = true,
    onNext: () -> Unit,
    onSkip: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.widthIn(max = 420.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row {
                Button(onClick = onSkip) { Text("Saltar tutorial") }
                if (showNextButton) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onNext) { Text(if (isLastStep) "Entendido" else "Siguiente") }
                }
            }
        }
    }
}
