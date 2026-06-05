
# TransactionScheduleResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **reasonId** | **kotlin.Long** |  |  [optional] |
| **reasonDesc** | **kotlin.String** |  |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **paymentMethod** | [**inline**](#PaymentMethod) |  |  [optional] |
| **transactionType** | [**inline**](#TransactionType) |  |  [optional] |


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



