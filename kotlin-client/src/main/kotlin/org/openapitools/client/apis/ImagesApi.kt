package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json


interface ImagesApi {
    /**
     * GET api/v1/images/{imageId}
     * Descargar imagen
     * Redirige a una URL prefirmada de corta duración después de validar que la imagen pertenece al usuario autenticado.
     * Responses:
     *  - 302: Redirección a SeaweedFS
     *  - 404: Imagen no encontrada
     *  - 401: Usuario no autenticado
     *
     * @param imageId ID de la imagen
     * @return [Unit]
     */
    @GET("api/v1/images/{imageId}")
    suspend fun downloadImage(@Path("imageId") imageId: kotlin.Long): Response<Unit>

}
