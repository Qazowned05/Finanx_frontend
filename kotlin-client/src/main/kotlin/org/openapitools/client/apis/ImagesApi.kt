package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import okhttp3.ResponseBody
import com.squareup.moshi.Json


interface ImagesApi {
    /**
     * GET images/{imageId}
     * Descargar imagen
     * Retorna los bytes de una imagen por su ID.
     * Responses:
     *  - 200: Imagen encontrada
     *  - 404: Imagen no encontrada
     *
     * @param imageId ID de la imagen
     * @return [ResponseBody]
     */
    @GET("images/{imageId}")
    suspend fun downloadImage(@Path("imageId") imageId: kotlin.Long): Response<ResponseBody>

}
