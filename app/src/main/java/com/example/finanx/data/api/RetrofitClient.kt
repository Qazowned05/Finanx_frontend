package com.example.finanx.data.api

import com.example.finanx.data.auth.TokenStore
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.openapitools.client.apis.AnalyticsApi
import org.openapitools.client.apis.AuthApi
import org.openapitools.client.apis.BudgetsApi
import org.openapitools.client.apis.CategoriesApi
import org.openapitools.client.apis.ChatBotApi
import org.openapitools.client.apis.NotificationsApi
import org.openapitools.client.apis.ReasonsApi
import org.openapitools.client.apis.SavingsApi
import org.openapitools.client.apis.ScheduleApi
import org.openapitools.client.apis.ScheduleBudgetsApi
import org.openapitools.client.apis.ScheduleTransactionsApi
import org.openapitools.client.apis.TransactionsApi
import org.openapitools.client.apis.UsersApi
import org.openapitools.client.infrastructure.Serializer
import org.openapitools.client.models.BudgetScheduleRequest
import org.openapitools.client.models.BudgetScheduleResponse
import org.openapitools.client.models.ScheduleRequest
import org.openapitools.client.models.ScheduleResponse
import org.openapitools.client.models.TransactionScheduleRequest
import org.openapitools.client.models.TransactionScheduleResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://a047-132-191-2-99.ngrok-free.app/"

    init {
        Serializer.moshiBuilder
            .add(
            PolymorphicJsonAdapterFactory.of(ScheduleRequest::class.java, "type")
                .withSubtype(TransactionScheduleRequest::class.java, "TRANSACTION")
                .withSubtype(BudgetScheduleRequest::class.java, "BUDGET")
            )
            .add(
                PolymorphicJsonAdapterFactory.of(ScheduleResponse::class.java, "type")
                    .withSubtype(TransactionScheduleResponse::class.java, "TRANSACTION")
                    .withSubtype(BudgetScheduleResponse::class.java, "BUDGET")
            )
    }

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

    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(AuthApi::class.java)
    }

    val budgetApi: BudgetsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(BudgetsApi::class.java)
    }

    val userApi: UsersApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(UsersApi::class.java)
    }

    val categoryApi: CategoriesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(CategoriesApi::class.java)
    }

    val savingGoalApi: SavingsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(SavingsApi::class.java)
    }

    val transactionApi: TransactionsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(TransactionsApi::class.java)
    }

    val notificationApi: NotificationsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(NotificationsApi::class.java)
    }

    val analyticsApi: AnalyticsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(AnalyticsApi::class.java)
    }

    val reasonApi: ReasonsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(ReasonsApi::class.java)
    }

    val budgetScheduleApi: ScheduleBudgetsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(ScheduleBudgetsApi::class.java)
    }

    val transactionScheduleApi: ScheduleTransactionsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(ScheduleTransactionsApi::class.java)
    }

    val scheduleApi: ScheduleApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(ScheduleApi::class.java)
    }

    val chatApi: ChatBotApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(ChatBotApi::class.java)
    }
}