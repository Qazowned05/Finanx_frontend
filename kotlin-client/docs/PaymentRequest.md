
# PaymentRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** | Id of the paying user |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Amount to charge |  |
| **currency** | **kotlin.String** | ISO currency code |  |
| **provider** | [**PaymentProvider**](PaymentProvider.md) |  |  |
| **subscriptionId** | **kotlin.Long** | Id of the subscription this payment belongs to, if any |  [optional] |
| **method** | [**PaymentMethod**](PaymentMethod.md) |  |  [optional] |
| **idempotencyKey** | **kotlin.String** | Client-supplied idempotency key, generated automatically when absent |  [optional] |
| **metadata** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Free-form metadata for this payment |  [optional] |



