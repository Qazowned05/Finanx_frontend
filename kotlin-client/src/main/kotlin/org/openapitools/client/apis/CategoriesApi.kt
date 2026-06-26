package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse

interface CategoriesApi {
    /**
     * POST categories
     * Crear categoria
     * Crea una nueva categoria.
     * Responses:
     *  - 201: Categoria creada
     *  - 400: Datos invalidos
     *
     * @param categoryRequest 
     * @return [CategoryResponse]
     */
    @POST("categories")
    suspend fun createCategory(@Body categoryRequest: CategoryRequest): Response<CategoryResponse>

    /**
     * POST categories/{id}/disable
     * Deshabilitar categoria
     * Deshabilita una categoria.
     * Responses:
     *  - 204: Deshabilitada
     *  - 404: No encontrada
     *
     * @param id ID de la categoria
     * @return [Unit]
     */
    @POST("categories/{id}/disable")
    suspend fun disableCategory(@Path("id") id: kotlin.Long): Response<Unit>

    /**
     * GET categories/user/{idUser}
     * Listar categorias por usuario
     * Obtiene todas las categorias asociadas a un usuario.
     * Responses:
     *  - 200: Listado obtenido
     *  - 404: Usuario no encontrado
     *
     * @param idUser ID del usuario
     * @return [kotlin.collections.List<CategoryResponse>]
     */
    @GET("categories/user/{idUser}")
    suspend fun findAllCategoriesByUser(@Path("idUser") idUser: kotlin.Long): Response<kotlin.collections.List<CategoryResponse>>

    /**
     * GET categories/user/{idUser}/{active}
     * Listar categorias por usuario y estado
     * Obtiene categorias filtradas por estado activo/inactivo.
     * Responses:
     *  - 200: Listado obtenido
     *
     * @param idUser ID del usuario
     * @param active Estado activo
     * @return [kotlin.collections.List<CategoryResponse>]
     */
    @GET("categories/user/{idUser}/{active}")
    suspend fun findAllCategoriesyUserAndActive(@Path("idUser") idUser: kotlin.Long, @Path("active") active: kotlin.Boolean): Response<kotlin.collections.List<CategoryResponse>>

    /**
     * GET categories/{id}
     * Buscar categoria por ID
     * Obtiene una categoria especifica.
     * Responses:
     *  - 200: Categoria encontrada
     *  - 404: No encontrada
     *
     * @param id ID de la categoria
     * @return [CategoryResponse]
     */
    @GET("categories/{id}")
    suspend fun findCategoryById(@Path("id") id: kotlin.Long): Response<CategoryResponse>

    /**
     * PUT categories/{id}
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
    @PUT("categories/{id}")
    suspend fun updateCategory(@Path("id") id: kotlin.Long, @Body categoryRequest: CategoryRequest): Response<CategoryResponse>

}
