# SubscriptionsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**activateSubscription**](SubscriptionsApi.md#activateSubscription) | **POST** api/v1/subscriptions/{id}/activate | Activate subscription |
| [**cancelSubscription**](SubscriptionsApi.md#cancelSubscription) | **POST** api/v1/subscriptions/{id}/cancel | Cancel subscription |
| [**createSubscription**](SubscriptionsApi.md#createSubscription) | **POST** api/v1/subscriptions | Create subscription |
| [**findSubscriptionById**](SubscriptionsApi.md#findSubscriptionById) | **GET** api/v1/subscriptions/{id} | Find subscription |
| [**getCurrentSubscription**](SubscriptionsApi.md#getCurrentSubscription) | **GET** api/v1/subscriptions/current | Get current subscription |
| [**listSubscriptions**](SubscriptionsApi.md#listSubscriptions) | **GET** api/v1/subscriptions | List subscriptions by user |
| [**renewSubscription**](SubscriptionsApi.md#renewSubscription) | **POST** api/v1/subscriptions/{id}/renew | Renew subscription |



Activate subscription

Activates a PENDING or PAST_DUE subscription and opens a new billing period.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | Subscription id

launch(Dispatchers.IO) {
    val result : SubscriptionResponse = webService.activateSubscription(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| Subscription id | |

### Return type

[**SubscriptionResponse**](SubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Cancel subscription

Cancels a subscription and disables auto renew.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | Subscription id

launch(Dispatchers.IO) {
    val result : SubscriptionResponse = webService.cancelSubscription(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| Subscription id | |

### Return type

[**SubscriptionResponse**](SubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Create subscription

Creates a new subscription in PENDING state, awaiting payment confirmation.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)
val subscriptionRequest : SubscriptionRequest =  // SubscriptionRequest | 

launch(Dispatchers.IO) {
    val result : SubscriptionResponse = webService.createSubscription(subscriptionRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **subscriptionRequest** | [**SubscriptionRequest**](SubscriptionRequest.md)|  | |

### Return type

[**SubscriptionResponse**](SubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Find subscription

Gets a subscription by id.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | Subscription id

launch(Dispatchers.IO) {
    val result : SubscriptionResponse = webService.findSubscriptionById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| Subscription id | |

### Return type

[**SubscriptionResponse**](SubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Get current subscription

Returns the active subscription of the authenticated user.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)

launch(Dispatchers.IO) {
    val result : SubscriptionResponse = webService.getCurrentSubscription()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SubscriptionResponse**](SubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


List subscriptions by user

Lists subscriptions of the authenticated user with optional state and pagination/sort parameters.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)
val state : SubscriptionState =  // SubscriptionState | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseSubscriptionResponse = webService.listSubscriptions(state, page, size, sort)
}
```

### Parameters
| **state** | [**SubscriptionState**](.md)|  | [optional] [enum: PENDING, ACTIVE, PAST_DUE, CANCELED, EXPIRED] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;createdAt,DESC&quot;)] |

### Return type

[**PageResponseSubscriptionResponse**](PageResponseSubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Renew subscription

Opens the next billing period for an active subscription.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | Subscription id

launch(Dispatchers.IO) {
    val result : SubscriptionResponse = webService.renewSubscription(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| Subscription id | |

### Return type

[**SubscriptionResponse**](SubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

