package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse

interface CategoryControllerApi {
    /**
     * POST categories
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param categoryRequest 
     * @return [CategoryResponse]
     */
    @POST("categories")
    suspend fun create4(@Body categoryRequest: CategoryRequest): Response<CategoryResponse>

    /**
     * POST categories/{id}/disable
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [Unit]
     */
    @POST("categories/{id}/disable")
    suspend fun disable4(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET categories/user/{idUser}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @return [kotlin.collections.List<CategoryResponse>]
     */
    @GET("categories/user/{idUser}")
    suspend fun findAllByUser3(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<CategoryResponse>>

    /**
     * GET categories/user/{idUser}/{active}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param idUser 
     * @param active 
     * @return [kotlin.collections.List<CategoryResponse>]
     */
    @GET("categories/user/{idUser}/{active}")
    suspend fun findAllByUserAndActive(@Path("idUser") idUser: kotlin.Long, @Path("active") active: kotlin.Boolean): Response<kotlin.collections.List<CategoryResponse>>

    /**
     * GET categories/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @return [CategoryResponse]
     */
    @GET("categories/{id}")
    suspend fun findById4(@Path("id") id: kotlin.Long): Response<CategoryResponse>

    /**
     * PUT categories/{id}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param id 
     * @param categoryRequest 
     * @return [CategoryResponse]
     */
    @PUT("categories/{id}")
    suspend fun update5(@Path("id") id: kotlin.Long, @Body categoryRequest: CategoryRequest): Response<CategoryResponse>

}
