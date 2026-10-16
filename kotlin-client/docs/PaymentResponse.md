
# PaymentResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Unique payment id |  [optional] |
| **userId** | **kotlin.Long** | Id of the paying user |  [optional] |
| **subscriptionId** | **kotlin.Long** | Id of the linked subscription, if any |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Charged amount |  [optional] |
| **currency** | **kotlin.String** | ISO currency code |  [optional] |
| **provider** | [**PaymentProvider**](PaymentProvider.md) |  |  [optional] |
| **method** | [**PaymentMethod**](PaymentMethod.md) |  |  [optional] |
| **state** | [**PaymentState**](PaymentState.md) |  |  [optional] |
| **externalReference** | **kotlin.String** | Reference assigned by the provider |  [optional] |
| **failureReason** | **kotlin.String** | Reason the payment failed, if applicable |  [optional] |
| **paidAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Date and time the payment was confirmed |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Creation date and time |  [optional] |
| **updatedAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Last update date and time |  [optional] |



