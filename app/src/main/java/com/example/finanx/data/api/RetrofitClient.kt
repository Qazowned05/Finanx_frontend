package com.example.finanx.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.apis.AuthControllerApi
import org.openapitools.client.apis.BudgetControllerApi
import org.openapitools.client.apis.UserControllerApi
import org.openapitools.client.infrastructure.Serializer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import org.openapitools.client.apis.CategoryControllerApi
import org.openapitools.client.apis.SavingGoalControllerApi
import okhttp3.Interceptor
import okhttp3.Response
object TokenStore {
    var token: String = ""
}
object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val moshi = Serializer.moshi

    private val authInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
        TokenManager.token?.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(request.build())
    }

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

<<<<<<< HEAD
    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()
=======

>>>>>>> 97c74ab (feat(login): Se agrego el interceptor de token para el login)

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
<<<<<<< HEAD

    val userApi: UserControllerApi by lazy {
=======
    val categoryApi: CategoryControllerApi by lazy {
>>>>>>> 97c74ab (feat(login): Se agrego el interceptor de token para el login)
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
<<<<<<< HEAD
            .create(UserControllerApi::class.java)
    }
=======
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



>>>>>>> 97c74ab (feat(login): Se agrego el interceptor de token para el login)
}
