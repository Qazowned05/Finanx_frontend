
# TransactionScheduleAnalytics

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** |  |  [optional] |
| **generatedAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **activeScheduledTransactionCount** | **kotlin.Long** |  |  [optional] |
| **activeCountByRecurrenceType** | **kotlin.collections.Map&lt;kotlin.String, kotlin.Long&gt;** |  |  [optional] |
| **projectionStart** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **projectionEnd** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **projectedIncomeAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **projectedSpentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **projectedNetAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **scheduledProjections** | [**kotlin.collections.List&lt;ScheduledProjection&gt;**](ScheduledProjection.md) |  |  [optional] |
| **insights** | [**kotlin.collections.List&lt;Insight&gt;**](Insight.md) |  |  [optional] |



