
# TransactionScheduleRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  |
| **paymentMethod** | [**inline**](#PaymentMethod) |  |  |
| **transactionType** | [**inline**](#TransactionType) |  |  |
| **reasonId** | **kotlin.Long** |  |  [optional] |
| **description** | **kotlin.String** |  |  [optional] |


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



