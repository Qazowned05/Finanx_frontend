
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
| **scheduledAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **state** | [**inline**](#State) |  |  [optional] |
| **transactionId** | **kotlin.Long** |  |  [optional] |
| **budgetDescription** | **kotlin.Long** |  |  [optional] |
| **budgetState** | [**inline**](#BudgetState) |  |  [optional] |
| **categoryName** | **kotlin.Long** |  |  [optional] |


<a id="Type"></a>
## Enum: type
| Name | Value |
| ---- | ----- |
| type | BUDGET_ALERT, EXPIRATION, REMINDER |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | SCHEDULED, SENT, READ, DISCARD |


<a id="BudgetState"></a>
## Enum: budgetState
| Name | Value |
| ---- | ----- |
| budgetState | ACTIVE, CANCELLED, FINALIZED |



