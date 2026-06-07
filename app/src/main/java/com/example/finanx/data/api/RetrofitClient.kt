package com.example.finanx.data.api

import okhttp3.OkHttpClient
import org.openapitools.client.apis.NotificationControllerApi
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import org.openapitools.client.apis.AuthControllerApi
import org.openapitools.client.apis.BudgetControllerApi
import org.openapitools.client.infrastructure.Serializer
import org.openapitools.client.apis.CategoryControllerApi
import org.openapitools.client.apis.SavingGoalControllerApi
import org.openapitools.client.apis.TransactionControllerApi
import okhttp3.Interceptor
import okhttp3.Response
object TokenStore {
    var token: String = ""
}
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val moshi = Serializer.moshi

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }



    val authApi: AuthControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(AuthControllerApi::class.java)
    }

    val budgetApi: BudgetControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(BudgetControllerApi::class.java)
    }
    val categoryApi: CategoryControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(CategoryControllerApi::class.java)
    }
    val savingGoalApi: SavingGoalControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(SavingGoalControllerApi::class.java)
    }

    val transactionApi: TransactionControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(TransactionControllerApi::class.java)
    }
//
val notificationApi: NotificationControllerApi by lazy {
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()
        .create(NotificationControllerApi::class.java)
} //// Componente visual para mostrar una notificación individual.

    // Interceptor de autenticación
    val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()

        val newRequest = if (TokenStore.token.isNotEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer ${TokenStore.token}")
                .build()
        } else {
            originalRequest
        }

        chain.proceed(newRequest)
    }
    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(authInterceptor)
        .build()
}
