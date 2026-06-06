# TransactionControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**create**](TransactionControllerApi.md#create) | **POST** transactions |  |
| [**disable**](TransactionControllerApi.md#disable) | **DELETE** transactions/{id} |  |
| [**findAllActiveByUser**](TransactionControllerApi.md#findAllActiveByUser) | **GET** transactions/user/{userId}/active |  |
| [**findAllByUser**](TransactionControllerApi.md#findAllByUser) | **GET** transactions/{userId} |  |
| [**findAllByUserBetween**](TransactionControllerApi.md#findAllByUserBetween) | **GET** transactions/user/{userId}/between |  |
| [**findById1**](TransactionControllerApi.md#findById1) | **GET** transactions/{id} |  |
| [**update1**](TransactionControllerApi.md#update1) | **PUT** transactions/{id} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionControllerApi::class.java)
val transactionRequest : TransactionRequest =  // TransactionRequest | 

launch(Dispatchers.IO) {
    val result : TransactionResponse = webService.create(transactionRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **transactionRequest** | [**TransactionRequest**](TransactionRequest.md)|  | |

### Return type

[**TransactionResponse**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.disable(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionResponse> = webService.findAllActiveByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;TransactionResponse&gt;**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionResponse> = webService.findAllByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;TransactionResponse&gt;**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val from : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val to : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionResponse> = webService.findAllByUserBetween(userId, from, to)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **from** | **java.time.LocalDate**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **to** | **java.time.LocalDate**|  | |

### Return type

[**kotlin.collections.List&lt;TransactionResponse&gt;**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : TransactionResponse = webService.findById1(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**TransactionResponse**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val transactionRequest : TransactionRequest =  // TransactionRequest | 

launch(Dispatchers.IO) {
    val result : TransactionResponse = webService.update1(id, transactionRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **transactionRequest** | [**TransactionRequest**](TransactionRequest.md)|  | |

### Return type

[**TransactionResponse**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

