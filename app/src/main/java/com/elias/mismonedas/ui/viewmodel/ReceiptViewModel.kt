package com.elias.mismonedas.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.ReceiptRepository
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.ReceiptResponse
import javax.inject.Inject

sealed class ScanState {
    object Idle : ScanState()
    object Scanning : ScanState()
    data class Success(val result: ReceiptResponse) : ScanState()
    data class Error(val message: String) : ScanState()
}

sealed class ImproveState {
    object Idle : ImproveState()
    object Improving : ImproveState()
    data class Success(val result: ReceiptResponse) : ImproveState()
    data class Error(val message: String) : ImproveState()
}

@HiltViewModel
class ReceiptViewModel @Inject constructor(
    private val repository: ReceiptRepository
) : ViewModel() {

    var scanState by mutableStateOf<ScanState>(ScanState.Idle)
        private set

    var lastScanResult by mutableStateOf<ReceiptResponse?>(null)
        private set

    var improveState by mutableStateOf<ImproveState>(ImproveState.Idle)
        private set

    fun scanReceipt(userId: Long, imageUri: Uri, context: Context) {
        viewModelScope.launch {
            scanState = ScanState.Scanning
            repository.scan(userId, imageUri, context).fold(
                onSuccess = {
                    lastScanResult = it
                    scanState = ScanState.Success(it)
                },
                onFailure = { scanState = ScanState.Error(it.message ?: "Error al escanear") }
            )
        }
    }

    fun improveScan(receiptId: Long, userId: Long) {
        viewModelScope.launch {
            improveState = ImproveState.Improving
            repository.improveScan(receiptId, userId).fold(
                onSuccess = {
                    lastScanResult = it
                    improveState = ImproveState.Success(it)
                },
                onFailure = { improveState = ImproveState.Error(it.message ?: "Error al mejorar") }
            )
        }
    }

    fun resetScanState() {
        scanState = ScanState.Idle
    }

    fun resetImproveState() {
        improveState = ImproveState.Idle
    }

    var receiptImageBytes by mutableStateOf<ByteArray?>(null)
        private set

    var receiptImageLoading by mutableStateOf(false)
        private set

    fun loadReceiptImage(imageId: Long) {
        if (receiptImageBytes != null || receiptImageLoading) {
            Log.d("ReceiptVM", "loadReceiptImage skipped: bytes=${receiptImageBytes?.size}, loading=$receiptImageLoading")
            return
        }
        viewModelScope.launch {
            receiptImageLoading = true
            Log.d("ReceiptVM", "loadReceiptImage loading imageId=$imageId")
            repository.getImage(imageId).fold(
                onSuccess = {
                    Log.d("ReceiptVM", "loadReceiptImage success: ${it.size} bytes")
                    receiptImageBytes = it
                },
                onFailure = {
                    Log.e("ReceiptVM", "loadReceiptImage failure: ${it.message}")
                    receiptImageBytes = null
                }
            )
            receiptImageLoading = false
        }
    }

    fun clearReceiptImage() {
        receiptImageBytes = null
        receiptImageLoading = false
    }
}
