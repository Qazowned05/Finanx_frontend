package com.example.finanx.data.api

import com.example.finanx.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface FinanxApi {
    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body request: UserRequest): Response<LoginResponse>

    @GET("/bugdets/{idUser}")
    suspend fun getBudgets(@Path("idUser") userId: Long): Response<List<BudgetResponse>>

    @POST("/bugdets")
    suspend fun createBudget(@Body request: BudgetRequest): Response<BudgetResponse>
}
