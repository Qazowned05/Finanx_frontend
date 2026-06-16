
# TransactionRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** |  |  |
| **categoryId** | **kotlin.Long** |  |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  |
| **paymentMethod** | [**inline**](#PaymentMethod) |  |  |
| **type** | [**inline**](#Type) |  |  |
| **reasonId** | **kotlin.Long** |  |  [optional] |
| **description** | **kotlin.String** |  |  [optional] |
| **savingGoalId** | **kotlin.Long** |  |  [optional] |


<a id="PaymentMethod"></a>
## Enum: paymentMethod
| Name | Value |
| ---- | ----- |
| paymentMethod | CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, YAPE, PLIN, OTHER |


<a id="Type"></a>
## Enum: type
| Name | Value |
| ---- | ----- |
| type | SPENT, INCOME |



