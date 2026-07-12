
# PaymentResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Unique payment id |  [optional] |
| **userId** | **kotlin.Long** | Id of the paying user |  [optional] |
| **subscriptionId** | **kotlin.Long** | Id of the linked subscription, if any |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Charged amount |  [optional] |
| **currency** | **kotlin.String** | ISO currency code |  [optional] |
| **provider** | [**inline**](#Provider) | Payment provider used |  [optional] |
| **method** | [**inline**](#Method) | Payment method used |  [optional] |
| **state** | [**inline**](#State) | Current payment state |  [optional] |
| **externalReference** | **kotlin.String** | Reference assigned by the provider |  [optional] |
| **failureReason** | **kotlin.String** | Reason the payment failed, if applicable |  [optional] |
| **paidAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Date and time the payment was confirmed |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Creation date and time |  [optional] |
| **updatedAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Last update date and time |  [optional] |


<a id="Provider"></a>
## Enum: provider
| Name | Value |
| ---- | ----- |
| provider | MERCADO_PAGO, CULQI, STRIPE, MANUAL_TRANSFER |


<a id="Method"></a>
## Enum: method
| Name | Value |
| ---- | ----- |
| method | CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, YAPE, PLIN, OTHER |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED, DISPUTED |



