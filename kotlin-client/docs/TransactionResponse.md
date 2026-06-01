
# TransactionResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** |  |  [optional] |
| **userId** | **kotlin.Long** |  |  [optional] |
| **categoryId** | **kotlin.Long** |  |  [optional] |
| **reasonId** | **kotlin.Long** |  |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **paymentMethod** | [**inline**](#PaymentMethod) |  |  [optional] |
| **type** | [**inline**](#Type) |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |


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



