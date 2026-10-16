package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import org.openapitools.client.models.PageResponseCategoryResponse
import org.openapitools.client.models.TransactionType

interface CategoriesApi {
    /**
     * POST api/v1/categories
     * Crear categoria
     * Crea una nueva categoria.
     * Responses:
     *  - 201: Creada
     *  - 400: Datos invalidos
     *
     * @param categoryRequest 
     * @return [CategoryResponse]
     */
    @POST("api/v1/categories")
    suspend fun createCategory(@Body categoryRequest: CategoryRequest): Response<CategoryResponse>

    /**
     * POST api/v1/categories/{id}/disable
     * Deshabilitar categoria
     * Deshabilita una categoria.
     * Responses:
     *  - 204: Deshabilitada
     *  - 404: No encontrada
     *
     * @param id ID de la categoria
     * @return [Unit]
     */
    @POST("api/v1/categories/{id}/disable")
    suspend fun disableCategory(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET api/v1/categories/{id}
     * Buscar categoria por ID
     * Obtiene una categoria especifica.
     * Responses:
     *  - 200: Categoria encontrada
     *  - 404: No encontrada
     *
     * @param id ID de la categoria
     * @return [CategoryResponse]
     */
    @GET("api/v1/categories/{id}")
    suspend fun findCategoryById(@Path("id") id: kotlin.Long): Response<CategoryResponse>

    /**
     * GET api/v1/categories
     * Listar categorias del usuario
     * Lista paginada del usuario autenticado con filtros opcionales active, type y search.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param active  (optional)
     * @param type  (optional)
     * @param search  (optional)
     * @param page Zero-based page index (0..N) (optional, default to 0)
     * @param size The size of the page to be returned (optional, default to 50)
     * @param sort Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. (optional, default to arrayListOf("name,ASC"))
     * @return [PageResponseCategoryResponse]
     */
    @GET("api/v1/categories")
    suspend fun listCategories(@Query("active") active: kotlin.Boolean? = null, @Query("type") type: TransactionType? = null, @Query("search") search: kotlin.String? = null, @Query("page") page: kotlin.Int? = 0, @Query("size") size: kotlin.Int? = 50, @Query("sort") sort: @JvmSuppressWildcards kotlin.collections.List<kotlin.String>? = arrayListOf("name,ASC")): Response<PageResponseCategoryResponse>

    /**
     * PUT api/v1/categories/{id}
     * Actualizar categoria
     * Actualiza una categoria existente.
     * Responses:
     *  - 200: Actualizada
     *  - 400: Datos invalidos
     *  - 404: No encontrada
     *
     * @param id ID de la categoria
     * @param categoryRequest 
     * @return [CategoryResponse]
     */
    @PUT("api/v1/categories/{id}")
    suspend fun updateCategory(@Path("id") id: kotlin.Long, @Body categoryRequest: CategoryRequest): Response<CategoryResponse>

}
