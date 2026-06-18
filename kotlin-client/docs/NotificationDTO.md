
# NotificationDTO

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** |  |  [optional] |
| **userId** | **kotlin.Long** |  |  [optional] |
| **userEmail** | **kotlin.String** |  |  [optional] |
| **type** | [**inline**](#Type) |  |  [optional] |
| **message** | **kotlin.String** |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **disabledAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **scheduledAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **sentAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **state** | [**inline**](#State) |  |  [optional] |
| **scheduleId** | **kotlin.Long** |  |  [optional] |
| **budgetId** | **kotlin.Long** |  |  [optional] |
| **budgetDescription** | **kotlin.String** |  |  [optional] |
| **budgetState** | [**inline**](#BudgetState) |  |  [optional] |
| **savingGoalId** | **kotlin.Long** |  |  [optional] |
| **savingGoalDescription** | **kotlin.String** |  |  [optional] |


<a id="Type"></a>
## Enum: type
| Name | Value |
| ---- | ----- |
| type | WARNING, SUCCESS, ERROR, INFO |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | SCHEDULED, SENT, READ, DISCARD |


<a id="BudgetState"></a>
## Enum: budgetState
| Name | Value |
| ---- | ----- |
| budgetState | ACTIVE, CANCELLED, DISABLED, FINALIZED |



