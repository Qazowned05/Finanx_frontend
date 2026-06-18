
# ScheduledProjection

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **scheduledTransactionId** | **kotlin.Long** |  |  [optional] |
| **transactionType** | [**inline**](#TransactionType) |  |  [optional] |
| **transactionAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **category** | [**CategoryResponse**](CategoryResponse.md) |  |  [optional] |
| **reason** | [**ReasonResponse**](ReasonResponse.md) |  |  [optional] |
| **recurrenceType** | [**inline**](#RecurrenceType) |  |  [optional] |
| **nextRunAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **endAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **expectedOccurrenceCount** | **kotlin.Int** |  |  [optional] |
| **expectedTotalAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |


<a id="TransactionType"></a>
## Enum: transactionType
| Name | Value |
| ---- | ----- |
| transactionType | SPENT, INCOME |


<a id="RecurrenceType"></a>
## Enum: recurrenceType
| Name | Value |
| ---- | ----- |
| recurrenceType | NONE, DAILY, WEEKLY, MONTHLY, YEARLY |



