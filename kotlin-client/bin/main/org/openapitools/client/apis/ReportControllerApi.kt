package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.BalanceReportRequest
import org.openapitools.client.models.BalanceReportResponse

interface ReportControllerApi {
    /**
     * POST reports/balance
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param balanceReportRequest 
     * @return [BalanceReportResponse]
     */
    @POST("reports/balance")
    suspend fun balance(@Body balanceReportRequest: BalanceReportRequest): Response<BalanceReportResponse>


    /**
    * enum for parameter fileType
    */
    enum class FileTypeExportBalance(val value: kotlin.String) {
        @Json(name = "CSV") CSV("CSV"),
        @Json(name = "PDF") PDF("PDF"),
        @Json(name = "EXCEL") EXCEL("EXCEL")
    }

    /**
     * POST reports/balance/export
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param fileType 
     * @param balanceReportRequest 
     * @return [kotlin.ByteArray]
     */
    @POST("reports/balance/export")
    suspend fun exportBalance(@Query("fileType") fileType: FileTypeExportBalance, @Body balanceReportRequest: BalanceReportRequest): Response<kotlin.ByteArray>

}
