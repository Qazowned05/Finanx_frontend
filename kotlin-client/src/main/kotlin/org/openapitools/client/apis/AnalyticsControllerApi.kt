package org.openapitools.client.apis

import org.openapitools.client.infrastructure.CollectionFormats.*
import retrofit2.http.*
import retrofit2.Response
import okhttp3.RequestBody
import com.squareup.moshi.Json

import org.openapitools.client.models.AnalyticsDashboard
import org.openapitools.client.models.AnalyticsPeriod
import org.openapitools.client.models.BudgetAnalytics
import org.openapitools.client.models.CategoryAggregate
import org.openapitools.client.models.CategoryAnalytics
import org.openapitools.client.models.Insight
import org.openapitools.client.models.RankingItemCategorySummary
import org.openapitools.client.models.RankingItemPaymentMethod
import org.openapitools.client.models.RankingItemReasonSummary
import org.openapitools.client.models.ReasonAnalytics
import org.openapitools.client.models.ScheduledProjection
import org.openapitools.client.models.TimeSeriesPoint
import org.openapitools.client.models.TransactionAggregate
import org.openapitools.client.models.TransactionAnalytics
import org.openapitools.client.models.TransactionScheduleAnalytics

interface AnalyticsControllerApi {
    /**
     * GET api/analytics/dashboard/{userId}
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param start 
     * @param end 
     * @return [AnalyticsDashboard]
     */
    @GET("api/analytics/dashboard/{userId}")
    suspend fun dashboard(@Path("userId") userId: kotlin.Long, @Query("start") start: java.time.LocalDateTime, @Query("end") end: java.time.LocalDateTime): Response<AnalyticsDashboard>

    /**
     * GET api/analytics/scheduled/active-count-by-recurrence
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [kotlin.collections.Map<kotlin.String, kotlin.Long>]
     */
    @GET("api/analytics/scheduled/active-count-by-recurrence")
    suspend fun getActiveScheduledCountByRecurrenceType(@Query("userId") userId: kotlin.Long): Response<kotlin.collections.Map<kotlin.String, kotlin.Long>>

    /**
     * GET api/analytics/scheduled/active-count
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @return [kotlin.Long]
     */
    @GET("api/analytics/scheduled/active-count")
    suspend fun getActiveScheduledTransactionCount(@Query("userId") userId: kotlin.Long): Response<kotlin.Long>

    /**
     * POST api/analytics/budgets/executions
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<BudgetAnalytics>]
     */
    @POST("api/analytics/budgets/executions")
    suspend fun getBudgetExecutions(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<BudgetAnalytics>>

    /**
     * POST api/analytics/budgets/insights
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<Insight>]
     */
    @POST("api/analytics/budgets/insights")
    suspend fun getBudgetInsights(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<Insight>>

    /**
     * POST api/analytics/budgets/overview
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [BudgetAnalytics]
     */
    @POST("api/analytics/budgets/overview")
    suspend fun getBudgetOverview(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<BudgetAnalytics>

    /**
     * POST api/analytics/categories/aggregates
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<CategoryAggregate>]
     */
    @POST("api/analytics/categories/aggregates")
    suspend fun getCategoryAggregates(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<CategoryAggregate>>

    /**
     * POST api/analytics/categories/overview
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [CategoryAnalytics]
     */
    @POST("api/analytics/categories/overview")
    suspend fun getCategoryOverview(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<CategoryAnalytics>

    /**
     * POST api/analytics/transactions/daily-net-series
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<TimeSeriesPoint>]
     */
    @POST("api/analytics/transactions/daily-net-series")
    suspend fun getDailyNetSeries(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<TimeSeriesPoint>>

    /**
     * POST api/analytics/budgets/exceeded
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<BudgetAnalytics>]
     */
    @POST("api/analytics/budgets/exceeded")
    suspend fun getExceededBudgets(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<BudgetAnalytics>>

    /**
     * POST api/analytics/transactions/most-used-payment-method
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.String]
     */
    @POST("api/analytics/transactions/most-used-payment-method")
    suspend fun getMostUsedPaymentMethod(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.String>

    /**
     * POST api/analytics/budgets/near-limit
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<BudgetAnalytics>]
     */
    @POST("api/analytics/budgets/near-limit")
    suspend fun getNearLimitBudgets(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<BudgetAnalytics>>

    /**
     * POST api/analytics/reasons/overview
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param topN 
     * @param analyticsPeriod 
     * @return [ReasonAnalytics]
     */
    @POST("api/analytics/reasons/overview")
    suspend fun getReasonOverview(@Query("userId") userId: kotlin.Long, @Query("topN") topN: kotlin.Int, @Body analyticsPeriod: AnalyticsPeriod): Response<ReasonAnalytics>

    /**
     * POST api/analytics/scheduled/insights
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param projectionStart 
     * @param projectionEnd 
     * @return [kotlin.collections.List<Insight>]
     */
    @POST("api/analytics/scheduled/insights")
    suspend fun getScheduledInsights(@Query("userId") userId: kotlin.Long, @Query("projectionStart") projectionStart: java.time.LocalDateTime, @Query("projectionEnd") projectionEnd: java.time.LocalDateTime): Response<kotlin.collections.List<Insight>>

    /**
     * POST api/analytics/scheduled/overview
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param projectionStart 
     * @param projectionEnd 
     * @return [TransactionScheduleAnalytics]
     */
    @POST("api/analytics/scheduled/overview")
    suspend fun getScheduledOverview(@Query("userId") userId: kotlin.Long, @Query("projectionStart") projectionStart: java.time.LocalDateTime, @Query("projectionEnd") projectionEnd: java.time.LocalDateTime): Response<TransactionScheduleAnalytics>

    /**
     * POST api/analytics/scheduled/projections
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param projectionStart 
     * @param projectionEnd 
     * @return [kotlin.collections.List<ScheduledProjection>]
     */
    @POST("api/analytics/scheduled/projections")
    suspend fun getScheduledProjections(@Query("userId") userId: kotlin.Long, @Query("projectionStart") projectionStart: java.time.LocalDateTime, @Query("projectionEnd") projectionEnd: java.time.LocalDateTime): Response<kotlin.collections.List<ScheduledProjection>>


    /**
    * enum for parameter type
    */
    enum class TypeGetTopCategories(val value: kotlin.String) {
        @Json(name = "SPENT") SPENT("SPENT"),
        @Json(name = "INCOME") INCOME("INCOME")
    }

    /**
     * POST api/analytics/categories/top
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param topN 
     * @param type 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<RankingItemCategorySummary>]
     */
    @POST("api/analytics/categories/top")
    suspend fun getTopCategories(@Query("userId") userId: kotlin.Long, @Query("topN") topN: kotlin.Int, @Query("type") type: TypeGetTopCategories, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<RankingItemCategorySummary>>

    /**
     * POST api/analytics/transactions/top-payment-methods
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param topN 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<RankingItemPaymentMethod>]
     */
    @POST("api/analytics/transactions/top-payment-methods")
    suspend fun getTopPaymentMethods(@Query("userId") userId: kotlin.Long, @Query("topN") topN: kotlin.Int, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<RankingItemPaymentMethod>>


    /**
    * enum for parameter type
    */
    enum class TypeGetTopReasons(val value: kotlin.String) {
        @Json(name = "SPENT") SPENT("SPENT"),
        @Json(name = "INCOME") INCOME("INCOME")
    }

    /**
     * POST api/analytics/reasons/top
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param topN 
     * @param type 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<RankingItemReasonSummary>]
     */
    @POST("api/analytics/reasons/top")
    suspend fun getTopReasons(@Query("userId") userId: kotlin.Long, @Query("topN") topN: kotlin.Int, @Query("type") type: TypeGetTopReasons, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<RankingItemReasonSummary>>

    /**
     * POST api/analytics/categories/total-amount-by-type
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.Map<kotlin.String, java.math.BigDecimal>]
     */
    @POST("api/analytics/categories/total-amount-by-type")
    suspend fun getTotalAmountByTransactionType(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.Map<kotlin.String, java.math.BigDecimal>>

    /**
     * POST api/analytics/transactions/aggregates/payment-method
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.Map<kotlin.String, TransactionAggregate>]
     */
    @POST("api/analytics/transactions/aggregates/payment-method")
    suspend fun getTransactionAggregatesByPaymentMethod(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.Map<kotlin.String, TransactionAggregate>>

    /**
     * POST api/analytics/transactions/aggregates/type
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.Map<kotlin.String, TransactionAggregate>]
     */
    @POST("api/analytics/transactions/aggregates/type")
    suspend fun getTransactionAggregatesByType(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.Map<kotlin.String, TransactionAggregate>>

    /**
     * POST api/analytics/transactions/insights
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<Insight>]
     */
    @POST("api/analytics/transactions/insights")
    suspend fun getTransactionInsights(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<Insight>>

    /**
     * POST api/analytics/transactions/overview
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [TransactionAnalytics]
     */
    @POST("api/analytics/transactions/overview")
    suspend fun getTransactionOverview(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<TransactionAnalytics>

    /**
     * POST api/analytics/user/insights
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param analyticsPeriod 
     * @return [kotlin.collections.List<Insight>]
     */
    @POST("api/analytics/user/insights")
    suspend fun getUserInsights(@Query("userId") userId: kotlin.Long, @Body analyticsPeriod: AnalyticsPeriod): Response<kotlin.collections.List<Insight>>


    /**
    * enum for parameter type
    */
    enum class TypeMakeProjection(val value: kotlin.String) {
        @Json(name = "SPENT") SPENT("SPENT"),
        @Json(name = "INCOME") INCOME("INCOME")
    }

    /**
     * POST api/analytics/scheduled/projection
     * 
     * 
     * Responses:
     *  - 200: OK
     *
     * @param userId 
     * @param projectionStart 
     * @param projectionEnd 
     * @param type 
     * @return [TransactionScheduleAnalytics]
     */
    @POST("api/analytics/scheduled/projection")
    suspend fun makeProjection(@Query("userId") userId: kotlin.Long, @Query("projectionStart") projectionStart: java.time.LocalDateTime, @Query("projectionEnd") projectionEnd: java.time.LocalDateTime, @Query("type") type: TypeMakeProjection): Response<TransactionScheduleAnalytics>

}
