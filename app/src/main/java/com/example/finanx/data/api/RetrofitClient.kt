package com.example.finanx.data.api

import com.example.finanx.data.auth.TokenStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object RetrofitClient {
    private const val BASE_URL = "https://7cf7-132-191-0-242.ngrok-free.app/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
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
        return Serializer.moshi
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
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

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideBudgetsApi(retrofit: Retrofit): BudgetsApi = retrofit.create(BudgetsApi::class.java)

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit): UsersApi = retrofit.create(UsersApi::class.java)

    @Provides
    @Singleton
    fun provideCategoriesApi(retrofit: Retrofit): CategoriesApi = retrofit.create(CategoriesApi::class.java)

    @Provides
    @Singleton
    fun provideSavingsApi(retrofit: Retrofit): SavingsApi = retrofit.create(SavingsApi::class.java)

    @Provides
    @Singleton
    fun provideTransactionsApi(retrofit: Retrofit): TransactionsApi = retrofit.create(
        TransactionsApi::class.java)

    @Provides
    @Singleton
    fun provideNotificationsApi(retrofit: Retrofit): NotificationsApi = retrofit.create(
        NotificationsApi::class.java)

    @Provides
    @Singleton
    fun provideAnalyticsApi(retrofit: Retrofit): AnalyticsApi = retrofit.create(AnalyticsApi::class.java)

    @Provides
    @Singleton
    fun provideReasonsApi(retrofit: Retrofit): ReasonsApi = retrofit.create(ReasonsApi::class.java)

    @Provides
    @Singleton
    fun provideScheduleBudgetsApi(retrofit: Retrofit): ScheduleBudgetsApi = retrofit.create(
        ScheduleBudgetsApi::class.java)

    @Provides
    @Singleton
    fun provideScheduleTransactionsApi(retrofit: Retrofit): ScheduleTransactionsApi = retrofit.create(
        ScheduleTransactionsApi::class.java)

    @Provides
    @Singleton
    fun provideScheduleApi(retrofit: Retrofit): ScheduleApi = retrofit.create(ScheduleApi::class.java)

    @Provides
    @Singleton
    fun provideChatBotApi(retrofit: Retrofit): ChatBotApi = retrofit.create(ChatBotApi::class.java)
}