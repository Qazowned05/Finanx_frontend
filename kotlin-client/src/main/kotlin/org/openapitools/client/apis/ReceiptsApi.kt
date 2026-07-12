package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.ReceiptResponse

import okhttp3.MultipartBody

interface ReceiptsApi {
    /**
     * GET receipts/{receiptId}
     * Obtener resultado de escaneo
     * Recupera el resultado de un escaneo previo por ID.
     * Responses:
     *  - 200: OK
     *
     * @param receiptId 
     * @param userId ID del usuario
     * @return [ReceiptResponse]
     */
    @GET("receipts/{receiptId}")
    suspend fun getScanResult(@Path("receiptId") receiptId: kotlin.Long, @Query("userId") userId: kotlin.Long): Response<ReceiptResponse>

    /**
     * POST receipts/{receiptId}/improve
     * Mejorar parseo con IA
     * Re-ejecuta GeminiParser sobre el texto OCR guardado. Actualiza el recibo solo si la confianza mejora.
     * Responses:
     *  - 200: OK
     *
     * @param receiptId ID del recibo
     * @param userId ID del usuario
     * @return [ReceiptResponse]
     */
    @POST("receipts/{receiptId}/improve")
    suspend fun improveScan(@Path("receiptId") receiptId: kotlin.Long, @Query("userId") userId: kotlin.Long): Response<ReceiptResponse>

    /**
     * POST receipts/scan
     * Escanear recibo con OCR
     * Procesa una imagen de recibo, extrae texto via Tesseract y parsea los datos (monto, fecha, comercio). Requiere plan PREMIUM.
     * Responses:
     *  - 200: Escaneo completado
     *  - 400: Imagen invalida o muy grande
     *  - 403: Plan no suficiente (PLAN_REQUIRED)
     *  - 503: Servicio ocupado (OcrBusyException)
     *
     * @param userId ID del usuario
     * @param image Imagen del recibo (max 5MB)
     * @return [ReceiptResponse]
     */
    @Multipart
    @POST("receipts/scan")
    suspend fun scanReceipt(@Query("userId") userId: kotlin.Long, @Part image: MultipartBody.Part): Response<ReceiptResponse>

}
