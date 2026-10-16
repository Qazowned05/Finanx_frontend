package com.elias.mismonedas.ui.screens

import android.Manifest
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.elias.mismonedas.ui.screens.scanner.CameraPreview
import com.elias.mismonedas.ui.screens.scanner.CapturedImagePreview
import com.elias.mismonedas.ui.screens.scanner.CropCaptureScreen
import com.elias.mismonedas.ui.screens.scanner.ResultScreen
import com.elias.mismonedas.ui.viewmodel.ImproveState
import com.elias.mismonedas.ui.viewmodel.ReceiptViewModel
import com.elias.mismonedas.ui.viewmodel.ScanState
import java.io.File

private const val TAG = "ScannerScreen"

private enum class ScannerPhase {
    LIVE,
    CAPTURED,
    SCANNING,
    RESULT
}

@Composable
fun ScannerScreen(
    receiptViewModel: ReceiptViewModel,
    userId: Long,
    onScanComplete: (
        amount: String,
        merchant: String,
        receiptId: Long,
        currency: String,
        paymentMethod: String,
        transactionType: String,
        categoryId: Long
    ) -> Unit,
    onBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    var hasCameraPermission by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf(ScannerPhase.LIVE) }
    var capturedUri by remember { mutableStateOf<Uri?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var showErrorMessage by remember { mutableStateOf<String?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            capturedUri = it
            phase = ScannerPhase.CAPTURED
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            Toast.makeText(context, "Permiso de cámara necesario", Toast.LENGTH_SHORT).show()
            onBack()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    LaunchedEffect(receiptViewModel.scanState) {
        when (val s = receiptViewModel.scanState) {
            is ScanState.Success -> {
                phase = ScannerPhase.RESULT
            }
            is ScanState.Error -> {
                showErrorMessage = s.message
                receiptViewModel.resetScanState()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!hasCameraPermission) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

        when (phase) {
            ScannerPhase.LIVE -> {
                CameraPreview(
                    imageCapture = { ic -> imageCapture = ic },
                    modifier = Modifier.fillMaxSize()
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Apunta la cámara al recibo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Elegir de galería")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            val ic = imageCapture ?: return@Button
                            captureImage(ic, context) { uri ->
                                capturedUri = uri
                                phase = ScannerPhase.CAPTURED
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Escanear", fontWeight = FontWeight.Bold)
                    }
                }
            }

            ScannerPhase.CAPTURED -> {
                val uri = capturedUri
                if (uri != null) {
                    CropCaptureScreen(
                        uri = uri,
                        onConfirm = { croppedBitmap ->
                            phase = ScannerPhase.SCANNING
                            val file = File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
                            file.outputStream().use { out ->
                                croppedBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 90, out)
                            }
                            val croppedUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                file
                            )
                            receiptViewModel.scanReceipt(croppedUri, context)
                        },
                        onRetry = {
                            capturedUri = null
                            phase = ScannerPhase.LIVE
                        }
                    )
                }
            }

            ScannerPhase.SCANNING -> {
                capturedUri?.let { uri ->
                    CapturedImagePreview(uri = uri)
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(32.dp)
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Escaneando recibo…",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            ScannerPhase.RESULT -> {
                val result = receiptViewModel.lastScanResult
                if (result != null) {
                    ResultScreen(
                        result = result,
                        capturedUri = capturedUri,
                        improveState = receiptViewModel.improveState,
                        onImprove = {
                            receiptViewModel.improveScan(result.receiptId ?: 0L)
                        },
                        onConfirm = {
                            val amount = result.detectedAmount?.toPlainString() ?: ""
                            val merchant = result.description ?: ""
                            val receiptId = result.receiptId ?: 0L
                            val currency = result.currencyGuess ?: "PEN"
                            val paymentMethod = result.paymentMethod?.value ?: ""
                            val transactionType = result.transactionType?.value ?: ""
                            val categoryId = result.detectedCategoryId ?: 0L
                            receiptViewModel.resetScanState()
                            receiptViewModel.resetImproveState()
                            capturedUri = null
                            onScanComplete(
                                amount, merchant, receiptId, currency,
                                paymentMethod, transactionType, categoryId
                            )
                        },
                        onRetry = {
                            receiptViewModel.resetScanState()
                            receiptViewModel.resetImproveState()
                            capturedUri = null
                            phase = ScannerPhase.LIVE
                        }
                    )
                }
            }
        }

        showErrorMessage?.let { message ->
            AlertDialog(
                onDismissRequest = { showErrorMessage = null },
                title = { Text("Error al escanear") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = {
                        showErrorMessage = null
                        capturedUri = null
                        phase = ScannerPhase.LIVE
                    }) {
                        Text("Aceptar")
                    }
                }
            )
        }
    }
}

private fun captureImage(
    imageCapture: ImageCapture,
    context: android.content.Context,
    onCaptured: (Uri) -> Unit
) {
    val photoFile = File(
        context.cacheDir,
        "scanner_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    photoFile
                )
                onCaptured(uri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Capture failed", exception)
                Toast.makeText(context, "Error al capturar foto", Toast.LENGTH_SHORT).show()
            }
        }
    )
}
