
# SubscriptionResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Unique subscription id |  [optional] |
| **userId** | **kotlin.Long** | Id of the subscribing user |  [optional] |
| **plan** | [**SubscriptionPlan**](SubscriptionPlan.md) |  |  [optional] |
| **billingCycle** | [**BillingCycle**](BillingCycle.md) |  |  [optional] |
| **state** | [**SubscriptionState**](SubscriptionState.md) |  |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Amount charged per billing cycle |  [optional] |
| **currency** | **kotlin.String** | ISO currency code |  [optional] |
| **autoRenew** | **kotlin.Boolean** | Whether the subscription renews automatically |  [optional] |
| **currentPeriodStart** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Start of the current billing period |  [optional] |
| **currentPeriodEnd** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | End of the current billing period |  [optional] |
| **canceledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Date and time the subscription was canceled, if applicable |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Creation date and time |  [optional] |
| **updatedAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Last update date and time |  [optional] |



