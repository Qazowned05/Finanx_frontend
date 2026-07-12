# PaymentsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createPayment**](PaymentsApi.md#createPayment) | **POST** payments | Create payment |
| [**findAllPaymentsBySubscription**](PaymentsApi.md#findAllPaymentsBySubscription) | **GET** payments/subscription/{subscriptionId} | List payments by subscription |
| [**findAllPaymentsByUser**](PaymentsApi.md#findAllPaymentsByUser) | **GET** payments/user/{userId} | List payments by user |
| [**findPaymentById**](PaymentsApi.md#findPaymentById) | **GET** payments/{id} | Find payment |
| [**refundPayment**](PaymentsApi.md#refundPayment) | **POST** payments/{id}/refund | Refund payment |



Create payment

Starts a new payment attempt against the configured provider.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(PaymentsApi::class.java)
val paymentRequest : PaymentRequest =  // PaymentRequest | 

launch(Dispatchers.IO) {
    val result : PaymentResponse = webService.createPayment(paymentRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **paymentRequest** | [**PaymentRequest**](PaymentRequest.md)|  | |

### Return type

[**PaymentResponse**](PaymentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


List payments by subscription

Gets all payments linked to a subscription.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(PaymentsApi::class.java)
val subscriptionId : kotlin.Long = 789 // kotlin.Long | Subscription id

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<PaymentResponse> = webService.findAllPaymentsBySubscription(subscriptionId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **subscriptionId** | **kotlin.Long**| Subscription id | |

### Return type

[**kotlin.collections.List&lt;PaymentResponse&gt;**](PaymentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


List payments by user

Gets all payments made by a user.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(PaymentsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | User id

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<PaymentResponse> = webService.findAllPaymentsByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**| User id | |

### Return type

[**kotlin.collections.List&lt;PaymentResponse&gt;**](PaymentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Find payment

Gets a payment by id.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(PaymentsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | Payment id

launch(Dispatchers.IO) {
    val result : PaymentResponse = webService.findPaymentById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| Payment id | |

### Return type

[**PaymentResponse**](PaymentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Refund payment

Requests a refund on the provider and marks the payment as REFUNDED.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(PaymentsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | Payment id

launch(Dispatchers.IO) {
    val result : PaymentResponse = webService.refundPayment(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| Payment id | |

### Return type

[**PaymentResponse**](PaymentResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

