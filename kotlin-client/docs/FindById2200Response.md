
# FindById2200Response

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **type** | **kotlin.String** |  |  |
| **id** | **kotlin.Long** |  |  [optional] |
| **userId** | **kotlin.Long** |  |  [optional] |
| **categoryId** | **kotlin.Long** |  |  [optional] |
| **categoryName** | **kotlin.String** |  |  [optional] |
| **nextRunAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **lastRunAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **active** | **kotlin.Boolean** |  |  [optional] |
| **state** | [**inline**](#State) |  |  [optional] |
| **recurrenceRule** | [**RecurrenceRuleResponse**](RecurrenceRuleResponse.md) |  |  [optional] |
| **alertable** | **kotlin.Boolean** |  |  [optional] |
| **percentAlert** | **kotlin.Int** |  |  [optional] |
| **limitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **description** | **kotlin.String** |  |  [optional] |
| **reasonId** | **kotlin.Long** |  |  [optional] |
| **reasonDesc** | **kotlin.String** |  |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **paymentMethod** | [**inline**](#PaymentMethod) |  |  [optional] |
| **transactionType** | [**inline**](#TransactionType) |  |  [optional] |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR |


<a id="PaymentMethod"></a>
## Enum: paymentMethod
| Name | Value |
| ---- | ----- |
| paymentMethod | CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, YAPE, PLIN, OTHER |


<a id="TransactionType"></a>
## Enum: transactionType
| Name | Value |
| ---- | ----- |
| transactionType | SPENT, INCOME |



