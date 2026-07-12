package com.elias.mismonedas.ui.screens.scanner

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

enum class DragHandle {
    NONE, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
    TOP, BOTTOM, LEFT, RIGHT
}

@Composable
fun CropCaptureScreen(
    uri: Uri,
    onConfirm: (Bitmap) -> Unit,
    onRetry: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current

    val bitmap = remember(uri) {
        try {
            context.contentResolver.openInputStream(uri)?.use {
                BitmapFactory.decodeStream(it)
            }
        } catch (e: Exception) {
            null
        }
    }

    if (bitmap == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("No se pudo cargar la imagen", color = Color.White)
        }
        return
    }

    val imageBitmap = remember(bitmap) { bitmap.asImageBitmap() }

    var cropLeft by remember { mutableFloatStateOf(0f) }
    var cropTop by remember { mutableFloatStateOf(0f) }
    var cropRight by remember { mutableFloatStateOf(1f) }
    var cropBottom by remember { mutableFloatStateOf(1f) }

    val currentCropLeft by rememberUpdatedState(cropLeft)
    val currentCropTop by rememberUpdatedState(cropTop)
    val currentCropRight by rememberUpdatedState(cropRight)
    val currentCropBottom by rememberUpdatedState(cropBottom)

    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    var activeHandle by remember { mutableStateOf(DragHandle.NONE) }

    val handleRadius = with(density) { 14.dp.toPx() }
    val minCropFraction = 0.1f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .onSizeChanged { containerSize = it }
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = "Foto capturada",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )

        if (containerSize.width > 0 && containerSize.height > 0) {
            val cropColor = Color(0xFFFF8C00)
            val dimColor = Color.Black.copy(alpha = 0.55f)

            val left = cropLeft * containerSize.width
            val top = cropTop * containerSize.height
            val right = cropRight * containerSize.width
            val bottom = cropBottom * containerSize.height
            val cropW = right - left
            val cropH = bottom - top

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(color = dimColor, topLeft = Offset(0f, 0f), size = Size(size.width, top))
                drawRect(color = dimColor, topLeft = Offset(0f, bottom), size = Size(size.width, size.height - bottom))
                drawRect(color = dimColor, topLeft = Offset(0f, top), size = Size(left, cropH))
                drawRect(color = dimColor, topLeft = Offset(right, top), size = Size(size.width - right, cropH))

                drawRect(
                    color = cropColor,
                    topLeft = Offset(left, top),
                    size = Size(cropW, cropH),
                    style = Stroke(width = 2.dp.toPx())
                )

                val cornerLen = 24.dp.toPx()
                listOf(
                    listOf(
                        Offset(left, top) to Offset(left + cornerLen, top),
                        Offset(left, top) to Offset(left, top + cornerLen)
                    ),
                    listOf(
                        Offset(right, top) to Offset(right - cornerLen, top),
                        Offset(right, top) to Offset(right, top + cornerLen)
                    ),
                    listOf(
                        Offset(left, bottom) to Offset(left + cornerLen, bottom),
                        Offset(left, bottom) to Offset(left, bottom - cornerLen)
                    ),
                    listOf(
                        Offset(right, bottom) to Offset(right - cornerLen, bottom),
                        Offset(right, bottom) to Offset(right, bottom - cornerLen)
                    )
                ).forEach { segments ->
                    segments.forEach { (start, end) ->
                        drawLine(color = cropColor, start = start, end = end, strokeWidth = 4.dp.toPx())
                    }
                }
            }

            val handleRadiusDp = with(density) { handleRadius.toDp() }
            val handleModifier = Modifier.size(handleRadiusDp * 2)

            Box(
                modifier = handleModifier
                    .offset(with(density) { (left - handleRadius).toDp() }, with(density) { (top - handleRadius).toDp() })
                    .background(cropColor, CircleShape)
            )
            Box(
                modifier = handleModifier
                    .offset(with(density) { (right - handleRadius).toDp() }, with(density) { (top - handleRadius).toDp() })
                    .background(cropColor, CircleShape)
            )
            Box(
                modifier = handleModifier
                    .offset(with(density) { (left - handleRadius).toDp() }, with(density) { (bottom - handleRadius).toDp() })
                    .background(cropColor, CircleShape)
            )
            Box(
                modifier = handleModifier
                    .offset(with(density) { (right - handleRadius).toDp() }, with(density) { (bottom - handleRadius).toDp() })
                    .background(cropColor, CircleShape)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                activeHandle = detectHandle(offset, currentCropLeft, currentCropTop, currentCropRight, currentCropBottom, size)
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (activeHandle == DragHandle.NONE) return@detectDragGestures
                                val w = size.width.toFloat()
                                val h = size.height.toFloat()
                                val dx = dragAmount.x / w
                                val dy = dragAmount.y / h

                                when (activeHandle) {
                                    DragHandle.TOP_LEFT -> {
                                        cropLeft = (currentCropLeft + dx).coerceIn(0f, currentCropRight - minCropFraction)
                                        cropTop = (currentCropTop + dy).coerceIn(0f, currentCropBottom - minCropFraction)
                                    }
                                    DragHandle.TOP_RIGHT -> {
                                        cropRight = (currentCropRight + dx).coerceIn(currentCropLeft + minCropFraction, 1f)
                                        cropTop = (currentCropTop + dy).coerceIn(0f, currentCropBottom - minCropFraction)
                                    }
                                    DragHandle.BOTTOM_LEFT -> {
                                        cropLeft = (currentCropLeft + dx).coerceIn(0f, currentCropRight - minCropFraction)
                                        cropBottom = (currentCropBottom + dy).coerceIn(currentCropTop + minCropFraction, 1f)
                                    }
                                    DragHandle.BOTTOM_RIGHT -> {
                                        cropRight = (currentCropRight + dx).coerceIn(currentCropLeft + minCropFraction, 1f)
                                        cropBottom = (currentCropBottom + dy).coerceIn(currentCropTop + minCropFraction, 1f)
                                    }
                                    DragHandle.TOP -> {
                                        cropTop = (currentCropTop + dy).coerceIn(0f, currentCropBottom - minCropFraction)
                                    }
                                    DragHandle.BOTTOM -> {
                                        cropBottom = (currentCropBottom + dy).coerceIn(currentCropTop + minCropFraction, 1f)
                                    }
                                    DragHandle.LEFT -> {
                                        cropLeft = (currentCropLeft + dx).coerceIn(0f, currentCropRight - minCropFraction)
                                    }
                                    DragHandle.RIGHT -> {
                                        cropRight = (currentCropRight + dx).coerceIn(currentCropLeft + minCropFraction, 1f)
                                    }
                                    DragHandle.NONE -> {}
                                }
                            },
                            onDragEnd = { activeHandle = DragHandle.NONE }
                        )
                    }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ajusta los bordes del recibo",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onRetry,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Reintentar")
                }

                Button(
                    onClick = {
                        val w = bitmap.width
                        val h = bitmap.height
                        val x = (cropLeft * w).toInt().coerceIn(0, w)
                        val y = (cropTop * h).toInt().coerceIn(0, h)
                        val x2 = (cropRight * w).toInt().coerceIn(0, w)
                        val y2 = (cropBottom * h).toInt().coerceIn(0, h)
                        val cw = (x2 - x).coerceAtLeast(1)
                        val ch = (y2 - y).coerceAtLeast(1)
                        val cropped = Bitmap.createBitmap(bitmap, x, y, cw, ch)
                        onConfirm(cropped)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Confirmar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun detectHandle(
    offset: Offset,
    cropLeft: Float,
    cropTop: Float,
    cropRight: Float,
    cropBottom: Float,
    size: IntSize
): DragHandle {
    val x = offset.x / size.width
    val y = offset.y / size.height
    val threshold = 0.05f

    val nearTop = kotlin.math.abs(y - cropTop) < threshold
    val nearBottom = kotlin.math.abs(y - cropBottom) < threshold
    val nearLeft = kotlin.math.abs(x - cropLeft) < threshold
    val nearRight = kotlin.math.abs(x - cropRight) < threshold

    if (nearTop && nearLeft) return DragHandle.TOP_LEFT
    if (nearTop && nearRight) return DragHandle.TOP_RIGHT
    if (nearBottom && nearLeft) return DragHandle.BOTTOM_LEFT
    if (nearBottom && nearRight) return DragHandle.BOTTOM_RIGHT
    if (nearTop) return DragHandle.TOP
    if (nearBottom) return DragHandle.BOTTOM
    if (nearLeft) return DragHandle.LEFT
    if (nearRight) return DragHandle.RIGHT

    return DragHandle.NONE
}
