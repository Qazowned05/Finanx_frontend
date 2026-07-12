
# SubscriptionResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Unique subscription id |  [optional] |
| **userId** | **kotlin.Long** | Id of the subscribing user |  [optional] |
| **plan** | [**inline**](#Plan) | Subscription plan |  [optional] |
| **billingCycle** | [**inline**](#BillingCycle) | Billing cycle |  [optional] |
| **state** | [**inline**](#State) | Current subscription state |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Amount charged per billing cycle |  [optional] |
| **currency** | **kotlin.String** | ISO currency code |  [optional] |
| **autoRenew** | **kotlin.Boolean** | Whether the subscription renews automatically |  [optional] |
| **currentPeriodStart** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Start of the current billing period |  [optional] |
| **currentPeriodEnd** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | End of the current billing period |  [optional] |
| **canceledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Date and time the subscription was canceled, if applicable |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Creation date and time |  [optional] |
| **updatedAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Last update date and time |  [optional] |


<a id="Plan"></a>
## Enum: plan
| Name | Value |
| ---- | ----- |
| plan | FREE, PREMIUM_TRIAL, PREMIUM |


<a id="BillingCycle"></a>
## Enum: billingCycle
| Name | Value |
| ---- | ----- |
| billingCycle | MONTHLY, ANNUAL |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | PENDING, ACTIVE, PAST_DUE, CANCELED, EXPIRED |



