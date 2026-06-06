
# ScheduleResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **type** | **kotlin.String** |  |  |
| **id** | **kotlin.Long** |  |  [optional] |
| **userId** | **kotlin.Long** |  |  [optional] |
| **categoryId** | **kotlin.Long** |  |  [optional] |
| **categoryName** | **kotlin.String** |  |  [optional] |
| **nextRunAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) |  |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) |  |  [optional] |
| **lastRunAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) |  |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) |  |  [optional] |
| **active** | **kotlin.Boolean** |  |  [optional] |
| **state** | [**inline**](#State) |  |  [optional] |
| **recurrenceRule** | [**RecurrenceRuleResponse**](RecurrenceRuleResponse.md) |  |  [optional] |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR |



