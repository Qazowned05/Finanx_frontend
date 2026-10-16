package com.elias.mismonedas.data.repository

import android.content.Context
import android.net.Uri
import com.elias.mismonedas.data.api.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.openapitools.client.apis.ReceiptsApi
import org.openapitools.client.models.ReceiptResponse
import java.io.IOException
import javax.inject.Inject

class ReceiptRepository @Inject constructor(
    private val api: ReceiptsApi,
    private val okHttpClient: OkHttpClient
) {
    suspend fun scan(imageUri: Uri, context: Context): Result<ReceiptResponse> =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                    ?: return@withContext Result.failure(IOException("No se pudo leer la imagen"))
                val bytes = inputStream.use { it.readBytes() }
                if (bytes.size > 5 * 1024 * 1024) {
                    return@withContext Result.failure(IOException("La imagen supera los 5MB"))
                }
                val requestBody = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("image", "receipt.jpg", requestBody)
                val response = api.scanReceipt(part)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Result.success(body)
                } else {
                    val error = response.errorBody()?.string() ?: "Error al escanear"
                    Result.failure(IOException(error))
                }
            } catch (e: Exception) {
                Result.failure(IOException("Error al escanear recibo: ${e.message}"))
            }
        }

    suspend fun getResult(receiptId: Long): Result<ReceiptResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getScanResult(receiptId)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Result.success(body)
                } else {
                    Result.failure(IOException("Error al obtener resultado del escaneo"))
                }
            } catch (e: Exception) {
                Result.failure(IOException("Error de red: ${e.message}"))
            }
        }

    suspend fun improveScan(receiptId: Long): Result<ReceiptResponse> =
        withContext(Dispatchers.IO) {
            try {
                val response = api.improveScan(receiptId)
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Result.success(body)
                } else {
                    val error = response.errorBody()?.string() ?: "Error al mejorar escaneo"
                    Result.failure(IOException(error))
                }
            } catch (e: Exception) {
                Result.failure(IOException("Error de red: ${e.message}"))
            }
        }

    // El backend responde 302 hacia una URL prefirmada de SeaweedFS;
    // OkHttp sigue el redirect por defecto y devolvemos los bytes.
    suspend fun getImage(imageId: Long): Result<ByteArray> =
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url("${RetrofitClient.BASE_URL}api/v1/images/$imageId")
                    .get()
                    .build()
                val response = okHttpClient.newCall(request).execute()
                response.use {
                    if (!it.isSuccessful) {
                        return@withContext Result.failure(
                            IOException("No se pudo cargar la imagen del recibo")
                        )
                    }
                    val bytes = it.body?.bytes()
                        ?: return@withContext Result.failure(
                            IOException("Respuesta vacía al cargar la imagen")
                        )
                    Result.success(bytes)
                }
            } catch (e: Exception) {
                Result.failure(IOException("Error de red: ${e.message}"))
            }
        }
}
