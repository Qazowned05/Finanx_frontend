
# Update2Request

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** |  |  |
| **categoryId** | **kotlin.Long** |  |  |
| **type** | **kotlin.String** |  |  |
| **alertable** | **kotlin.Boolean** |  |  |
| **percentAlert** | **kotlin.Int** |  |  |
| **limitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  |
| **description** | **kotlin.String** |  |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  |
| **paymentMethod** | [**inline**](#PaymentMethod) |  |  |
| **transactionType** | [**inline**](#TransactionType) |  |  |
| **recurrenceRule** | [**RecurrenceRuleRequest**](RecurrenceRuleRequest.md) |  |  [optional] |
| **reasonId** | **kotlin.Long** |  |  [optional] |


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



