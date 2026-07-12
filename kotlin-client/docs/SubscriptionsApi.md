# SubscriptionsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**activateSubscription**](SubscriptionsApi.md#activateSubscription) | **POST** subscriptions/{id}/activate | Activate subscription |
| [**cancelSubscription**](SubscriptionsApi.md#cancelSubscription) | **POST** subscriptions/{id}/cancel | Cancel subscription |
| [**createSubscription**](SubscriptionsApi.md#createSubscription) | **POST** subscriptions | Create subscription |
| [**findAllSubscriptionsByUser**](SubscriptionsApi.md#findAllSubscriptionsByUser) | **GET** subscriptions/user/{userId} | List subscriptions by user |
| [**findSubscriptionById**](SubscriptionsApi.md#findSubscriptionById) | **GET** subscriptions/{id} | Find subscription |
| [**renewSubscription**](SubscriptionsApi.md#renewSubscription) | **POST** subscriptions/{id}/renew | Renew subscription |



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


List subscriptions by user

Gets all subscriptions belonging to a user.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SubscriptionsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | User id

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<SubscriptionResponse> = webService.findAllSubscriptionsByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**| User id | |

### Return type

[**kotlin.collections.List&lt;SubscriptionResponse&gt;**](SubscriptionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
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

