# SavingGoalControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelSavingGoal**](SavingGoalControllerApi.md#cancelSavingGoal) | **POST** savings/{id}/cancel |  |
| [**createSavingGoal**](SavingGoalControllerApi.md#createSavingGoal) | **POST** savings |  |
| [**disableSavingGoal**](SavingGoalControllerApi.md#disableSavingGoal) | **POST** savings/{id}/disable |  |
| [**findSavingGoalById**](SavingGoalControllerApi.md#findSavingGoalById) | **GET** savings/{id} |  |
| [**findSavingGoalsByUser**](SavingGoalControllerApi.md#findSavingGoalsByUser) | **GET** savings/user/{idUser} |  |
| [**updateSavingGoal**](SavingGoalControllerApi.md#updateSavingGoal) | **PUT** savings/{id} |  |





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
    webService.cancelSavingGoal(id)
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
    val result : SavingGoalResponse = webService.createSavingGoal(savingGoalRequest)
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
    webService.disableSavingGoal(id)
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
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.findSavingGoalById(id)
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
val idUser : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<SavingGoalResponse> = webService.findSavingGoalsByUser(idUser)
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
val savingGoalRequest : SavingGoalRequest =  // SavingGoalRequest | 

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.updateSavingGoal(id, savingGoalRequest)
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

