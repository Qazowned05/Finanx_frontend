
# SubscriptionRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** | Id of the subscribing user |  |
| **plan** | [**inline**](#Plan) | Subscription plan |  |
| **billingCycle** | [**inline**](#BillingCycle) | Billing cycle |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Amount charged per billing cycle |  |
| **currency** | **kotlin.String** | ISO currency code |  |
| **autoRenew** | **kotlin.Boolean** | Whether the subscription renews automatically |  [optional] |


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



