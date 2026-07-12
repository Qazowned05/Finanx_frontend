package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.UserPreferencesRequest
import org.openapitools.client.models.UserPreferencesResponse

interface UserPreferencesApi {
    /**
     * POST users/{userId}/preferences
     * Crear preferencias del usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param userPreferencesRequest 
     * @return [UserPreferencesResponse]
     */
    @POST("users/{userId}/preferences")
    suspend fun createUserPreferences(@Path("userId") userId: kotlin.Long, @Body userPreferencesRequest: UserPreferencesRequest): Response<UserPreferencesResponse>

    /**
     * GET users/{userId}/preferences
     * Obtener preferencias del usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [UserPreferencesResponse]
     */
    @GET("users/{userId}/preferences")
    suspend fun getUserPreferences(@Path("userId") userId: kotlin.Long): Response<UserPreferencesResponse>

    /**
     * PUT users/{userId}/preferences
     * Actualizar preferencias del usuario
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param userPreferencesRequest 
     * @return [UserPreferencesResponse]
     */
    @PUT("users/{userId}/preferences")
    suspend fun updateUserPreferences(@Path("userId") userId: kotlin.Long, @Body userPreferencesRequest: UserPreferencesRequest): Response<UserPreferencesResponse>

}
