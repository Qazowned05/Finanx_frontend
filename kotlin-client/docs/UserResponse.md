
# UserResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** |  |  [optional] |
| **name** | **kotlin.String** |  |  [optional] |
| **lastname** | **kotlin.String** |  |  [optional] |
| **email** | **kotlin.String** |  |  [optional] |
| **username** | **kotlin.String** |  |  [optional] |
| **role** | [**inline**](#Role) |  |  [optional] |
| **state** | [**inline**](#State) |  |  [optional] |
| **moneyBalance** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **createdAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **disabledAt** | [**java.time.OffsetDateTime**](java.time.OffsetDateTime.md) |  |  [optional] |
| **fullName** | **kotlin.String** |  |  [optional] |
| **timeZone** | [**inline**](#TimeZone) |  |  [optional] |


<a id="Role"></a>
## Enum: role
| Name | Value |
| ---- | ----- |
| role | USER, SYSTEM |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | ENABLED, BLOCKED, DISABLED |


<a id="TimeZone"></a>
## Enum: timeZone
| Name | Value |
| ---- | ----- |
| timeZone | AMERICA_LIMA, AMERICA_BOGOTA, AMERICA_MEXICO_CITY, AMERICA_SANTIAGO, AMERICA_BUENOS_AIRES, AMERICA_NEW_YORK, AMERICA_LOS_ANGELES |



