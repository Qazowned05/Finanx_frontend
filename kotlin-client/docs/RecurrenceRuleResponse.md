
# RecurrenceRuleResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** |  |  [optional] |
| **recurrenceType** | [**inline**](#RecurrenceType) |  |  [optional] |
| **interval** | **kotlin.Int** |  |  [optional] |
| **dayOfWeek** | [**inline**](#DayOfWeek) |  |  [optional] |
| **start** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **end** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **durationDays** | **kotlin.Long** |  |  [optional] |


<a id="RecurrenceType"></a>
## Enum: recurrenceType
| Name | Value |
| ---- | ----- |
| recurrenceType | NONE, DAILY, WEEKLY, MONTHLY, YEARLY |


<a id="DayOfWeek"></a>
## Enum: dayOfWeek
| Name | Value |
| ---- | ----- |
| dayOfWeek | MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY |



