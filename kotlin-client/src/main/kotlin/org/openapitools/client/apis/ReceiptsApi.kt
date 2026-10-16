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
     * GET api/v1/receipts/{receiptId}
     * Obtener resultado de escaneo
     * Recupera el resultado de un escaneo previo por ID.
     * Responses:
     *  - 200: OK
     *
     * @param receiptId 
     * @return [ReceiptResponse]
     */
    @GET("api/v1/receipts/{receiptId}")
    suspend fun getScanResult(@Path("receiptId") receiptId: kotlin.Long): Response<ReceiptResponse>

    /**
     * POST api/v1/receipts/{receiptId}/improve
     * Mejorar parseo con IA
     * Re-ejecuta GeminiParser sobre el texto OCR guardado. Actualiza el recibo solo si la confianza mejora.
     * Responses:
     *  - 200: OK
     *
     * @param receiptId ID del recibo
     * @return [ReceiptResponse]
     */
    @POST("api/v1/receipts/{receiptId}/improve")
    suspend fun improveScan(@Path("receiptId") receiptId: kotlin.Long): Response<ReceiptResponse>

    /**
     * POST api/v1/receipts/scan
     * Encolar escaneo OCR de recibo
     * Sube la imagen, crea el recibo en estado PENDING y encola el OCR en el microservicio doc-processing. Responde 202 con la Location para consultar el resultado. Requiere plan PREMIUM.
     * Responses:
     *  - 202: Escaneo encolado; consultar estado en Location
     *  - 400: Imagen invalida o muy grande
     *  - 403: Plan no suficiente (PLAN_REQUIRED)
     *
     * @param image Imagen del recibo (max 5MB)
     * @return [ReceiptResponse]
     */
    @Multipart
    @POST("api/v1/receipts/scan")
    suspend fun scanReceipt(@Part image: MultipartBody.Part): Response<ReceiptResponse>

}
