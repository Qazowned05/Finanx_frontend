# SavingGoalControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancel1**](SavingGoalControllerApi.md#cancel1) | **POST** savings/{id}/cancel |  |
| [**create2**](SavingGoalControllerApi.md#create2) | **POST** savings |  |
| [**disable3**](SavingGoalControllerApi.md#disable3) | **POST** savings/{id}/disable |  |
| [**findAllActiveByUser1**](SavingGoalControllerApi.md#findAllActiveByUser1) | **GET** savings/user/{idUser} |  |
| [**findById2**](SavingGoalControllerApi.md#findById2) | **GET** savings/{id} |  |
| [**update3**](SavingGoalControllerApi.md#update3) | **PUT** savings/{id} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingGoalControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.cancel1(id)
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
val webService = apiClient.createWebservice(SavingGoalControllerApi::class.java)
val savingGoalRequest : SavingGoalRequest =  // SavingGoalRequest | 

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.create2(savingGoalRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **savingGoalRequest** | [**SavingGoalRequest**](SavingGoalRequest.md)|  | |

### Return type

[**SavingGoalResponse**](SavingGoalResponse.md)

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
val webService = apiClient.createWebservice(SavingGoalControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.disable3(id)
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
val webService = apiClient.createWebservice(SavingGoalControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<SavingGoalResponse> = webService.findAllActiveByUser1(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;SavingGoalResponse&gt;**](SavingGoalResponse.md)

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
val webService = apiClient.createWebservice(SavingGoalControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.findById2(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**SavingGoalResponse**](SavingGoalResponse.md)

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
val webService = apiClient.createWebservice(SavingGoalControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val savingGoalRequest : SavingGoalRequest =  // SavingGoalRequest | 

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.update3(id, savingGoalRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **savingGoalRequest** | [**SavingGoalRequest**](SavingGoalRequest.md)|  | |

### Return type

[**SavingGoalResponse**](SavingGoalResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

