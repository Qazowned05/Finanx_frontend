package com.example.finanx.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.apis.ReasonControllerApi
import org.openapitools.client.apis.ScheduleControllerApi
import org.openapitools.client.apis.TransactionScheduleControllerApi
import org.openapitools.client.apis.AuthControllerApi
import org.openapitools.client.apis.BudgetControllerApi
import org.openapitools.client.apis.CategoryControllerApi
import org.openapitools.client.apis.SavingGoalControllerApi
import org.openapitools.client.apis.TransactionControllerApi
import org.openapitools.client.apis.UserControllerApi
import org.openapitools.client.infrastructure.Serializer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import org.openapitools.client.apis.NotificationControllerApi
import org.openapitools.client.apis.AnalyticsControllerApi
import org.openapitools.client.apis.BudgetScheduleControllerApi

object TokenStore {
    var token: String = ""
}

object RetrofitClient {
    private const val BASE_URL = "http://34.95.232.182/"

    private val moshi = Serializer.moshi

    private val authInterceptor = Interceptor { chain ->
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

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()

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

    val userApi: UserControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(UserControllerApi::class.java)
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

    val notificationApi: NotificationControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(NotificationControllerApi::class.java)
    }

    val analyticsApi: AnalyticsControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(AnalyticsControllerApi::class.java)
    }

    val reasonApi: ReasonControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(ReasonControllerApi::class.java)
    }

    val budgetScheduleApi: BudgetScheduleControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(BudgetScheduleControllerApi::class.java)
    }

    val transactionScheduleApi: TransactionScheduleControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(TransactionScheduleControllerApi::class.java)
    }

    val scheduleApi: ScheduleControllerApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(ScheduleControllerApi::class.java)
    }
}