
# PaymentRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** | Id of the paying user |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Amount to charge |  |
| **currency** | **kotlin.String** | ISO currency code |  |
| **provider** | [**inline**](#Provider) | Payment provider used for this attempt |  |
| **subscriptionId** | **kotlin.Long** | Id of the subscription this payment belongs to, if any |  [optional] |
| **method** | [**inline**](#Method) | Payment method used |  [optional] |
| **idempotencyKey** | **kotlin.String** | Client-supplied idempotency key, generated automatically when absent |  [optional] |
| **metadata** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Free-form metadata for this payment |  [optional] |


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



