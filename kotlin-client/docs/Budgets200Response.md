
# Budgets200Response

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **type** | **kotlin.String** |  |  |
| **periodResponse** | [**PeriodResponse**](PeriodResponse.md) |  |  [optional] |
| **title** | **kotlin.String** |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **insightSummary** | [**InsightSummary**](InsightSummary.md) |  |  [optional] |
| **aggregate** | [**Aggregate**](Aggregate.md) |  |  [optional] |
| **byHealth** | [**kotlin.collections.List&lt;AggregateByBudgetHealth&gt;**](AggregateByBudgetHealth.md) |  |  [optional] |
| **byCategory** | [**kotlin.collections.List&lt;AggregateByBudgetExecutionResponse&gt;**](AggregateByBudgetExecutionResponse.md) |  |  [optional] |
| **executionEvolution** | [**TimeLineBudgetExecutionResponse**](TimeLineBudgetExecutionResponse.md) |  |  [optional] |
| **mostExecuted** | [**RankingCategoryResponse**](RankingCategoryResponse.md) |  |  [optional] |
| **mostRemaining** | [**RankingCategoryResponse**](RankingCategoryResponse.md) |  |  [optional] |
| **totalBudgetLimitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **totalBudgetSpentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **totalBudgetRemainingAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **byState** | [**kotlin.collections.List&lt;AggregateBySavingGoalState&gt;**](AggregateBySavingGoalState.md) |  |  [optional] |
| **contributionEvolution** | [**TimeLineSavingGoalResponse**](TimeLineSavingGoalResponse.md) |  |  [optional] |
| **dailyContributions** | [**TimeLineLocalDate**](TimeLineLocalDate.md) |  |  [optional] |
| **closestToGoal** | [**RankingSavingGoalResponse**](RankingSavingGoalResponse.md) |  |  [optional] |
| **mostContributed** | [**RankingSavingGoalResponse**](RankingSavingGoalResponse.md) |  |  [optional] |
| **activeScheduledTransactionCount** | **kotlin.Long** |  |  [optional] |
| **activeCountByRecurrenceType** | **kotlin.collections.Map&lt;kotlin.String, kotlin.Long&gt;** |  |  [optional] |
| **projectionStart** | [**java.time.LocalDate**](java.time.LocalDate.md) |  |  [optional] |
| **projectionEnd** | [**java.time.LocalDate**](java.time.LocalDate.md) |  |  [optional] |
| **projectedIncomeAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **projectedSpentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **projectedNetAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **scheduledProjections** | [**kotlin.collections.List&lt;ScheduledProjection&gt;**](ScheduledProjection.md) |  |  [optional] |
| **insights** | [**kotlin.collections.List&lt;Insight&gt;**](Insight.md) |  |  [optional] |
| **summaryByCategory** | [**RankingCategorySummary**](RankingCategorySummary.md) |  |  [optional] |
| **summaryByPaymentMethod** | [**RankingPaymentMethod**](RankingPaymentMethod.md) |  |  [optional] |
| **summaryByType** | [**RankingTransactionType**](RankingTransactionType.md) |  |  [optional] |
| **dailyExpenses** | [**TimeLineTransactionResponse**](TimeLineTransactionResponse.md) |  |  [optional] |
| **dailyIncome** | [**TimeLineTransactionResponse**](TimeLineTransactionResponse.md) |  |  [optional] |
| **topCategories** | [**RankingCategorySummary**](RankingCategorySummary.md) |  |  [optional] |
| **topSpentReasons** | [**RankingReasonSummary**](RankingReasonSummary.md) |  |  [optional] |
| **topIncomeReasons** | [**RankingReasonSummary**](RankingReasonSummary.md) |  |  [optional] |



