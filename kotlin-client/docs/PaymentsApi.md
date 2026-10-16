# PaymentsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createPayment**](PaymentsApi.md#createPayment) | **POST** api/v1/payments | Create payment |
| [**findPaymentById**](PaymentsApi.md#findPaymentById) | **GET** api/v1/payments/{id} | Find payment |
| [**listPayments**](PaymentsApi.md#listPayments) | **GET** api/v1/payments | List payments by user |
| [**refundPayment**](PaymentsApi.md#refundPayment) | **POST** api/v1/payments/{id}/refund | Refund payment |



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


List payments by user

Lists payments of the authenticated user with optional state, subscriptionId and from/to filters, plus pagination and sort.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(PaymentsApi::class.java)
val state : PaymentState =  // PaymentState | 
val subscriptionId : kotlin.Long = 789 // kotlin.Long | 
val from : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val to : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponsePaymentResponse = webService.listPayments(state, subscriptionId, from, to, page, size, sort)
}
```

### Parameters
| **state** | [**PaymentState**](.md)|  | [optional] [enum: PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED, DISPUTED] |
| **subscriptionId** | **kotlin.Long**|  | [optional] |
| **from** | **java.time.LocalDate**|  | [optional] |
| **to** | **java.time.LocalDate**|  | [optional] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;createdAt,DESC&quot;)] |

### Return type

[**PageResponsePaymentResponse**](PageResponsePaymentResponse.md)

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

