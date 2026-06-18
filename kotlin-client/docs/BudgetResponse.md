
# BudgetResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** |  |  [optional] |
| **userId** | **kotlin.Long** |  |  [optional] |
| **categoryId** | **kotlin.Long** |  |  [optional] |
| **categoryName** | **kotlin.String** |  |  [optional] |
| **scheduleId** | **kotlin.Long** |  |  [optional] |
| **alertable** | **kotlin.Boolean** |  |  [optional] |
| **percentAlert** | **kotlin.Int** |  |  [optional] |
| **limitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **description** | **kotlin.String** |  |  [optional] |
| **state** | [**inline**](#State) |  |  [optional] |
| **health** | [**inline**](#Health) |  |  [optional] |
| **start** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **end** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **disabledAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | ACTIVE, CANCELLED, DISABLED, FINALIZED |


<a id="Health"></a>
## Enum: health
| Name | Value |
| ---- | ----- |
| health | OK, NEAR_LIMIT, EXCEEDED, INACTIVE |



