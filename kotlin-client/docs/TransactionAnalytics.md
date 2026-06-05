
# TransactionAnalytics

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** |  |  [optional] |
| **period** | [**AnalyticsPeriod**](AnalyticsPeriod.md) |  |  [optional] |
| **totalIncomeAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **totalSpentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **netAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **aggregatesByTransactionType** | [**kotlin.collections.Map&lt;kotlin.String, TransactionAggregate&gt;**](TransactionAggregate.md) |  |  [optional] |
| **aggregatesByPaymentMethod** | [**kotlin.collections.Map&lt;kotlin.String, TransactionAggregate&gt;**](TransactionAggregate.md) |  |  [optional] |
| **mostUsedPaymentMethod** | [**inline**](#MostUsedPaymentMethod) |  |  [optional] |
| **topPaymentMethods** | [**kotlin.collections.List&lt;RankingItemPaymentMethod&gt;**](RankingItemPaymentMethod.md) |  |  [optional] |
| **dailyNetAmounts** | [**kotlin.collections.List&lt;TimeSeriesPoint&gt;**](TimeSeriesPoint.md) |  |  [optional] |
| **insights** | [**kotlin.collections.List&lt;Insight&gt;**](Insight.md) |  |  [optional] |


<a id="MostUsedPaymentMethod"></a>
## Enum: mostUsedPaymentMethod
| Name | Value |
| ---- | ----- |
| mostUsedPaymentMethod | CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, YAPE, PLIN, OTHER |



