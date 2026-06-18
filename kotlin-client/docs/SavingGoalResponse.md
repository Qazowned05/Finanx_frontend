
# SavingGoalResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** |  |  [optional] |
| **description** | **kotlin.String** |  |  [optional] |
| **targetAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **accumulated** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **deadline** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **completedAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **disabledAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **state** | [**inline**](#State) |  |  [optional] |
| **active** | **kotlin.Boolean** |  |  [optional] |
| **userId** | **kotlin.Long** |  |  [optional] |
| **progressPercentage** | **kotlin.Double** |  |  [optional] |
| **transactionsCount** | **kotlin.Int** |  |  [optional] |
| **outstanding** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **daysRemaining** | **kotlin.Long** |  |  [optional] |
| **averageContribution** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **completed** | **kotlin.Boolean** |  |  [optional] |
| **estimatedCompletionDate** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | RUNNING, COMPLETED, CANCELLED |



