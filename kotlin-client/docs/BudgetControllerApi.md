# BudgetControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancel**](BudgetControllerApi.md#cancel) | **POST** bugdets/{id}/cancel |  |
| [**create5**](BudgetControllerApi.md#create5) | **POST** bugdets |  |
| [**disable5**](BudgetControllerApi.md#disable5) | **POST** bugdets/{id}/disable |  |
| [**findAllByUser3**](BudgetControllerApi.md#findAllByUser3) | **GET** bugdets/{idUser} |  |
| [**findAllByUserAndState1**](BudgetControllerApi.md#findAllByUserAndState1) | **GET** bugdets/{idUser}/{state} |  |
| [**findById6**](BudgetControllerApi.md#findById6) | **GET** bugdets/{id} |  |
| [**update6**](BudgetControllerApi.md#update6) | **PUT** bugdets/{id} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.cancel(id)
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
val webService = apiClient.createWebservice(BudgetControllerApi::class.java)
val budgetRequest : BudgetRequest =  // BudgetRequest | 

launch(Dispatchers.IO) {
    val result : BudgetResponse = webService.create5(budgetRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **budgetRequest** | [**BudgetRequest**](BudgetRequest.md)|  | |

### Return type

[**BudgetResponse**](BudgetResponse.md)

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
val webService = apiClient.createWebservice(BudgetControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.disable5(id)
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
val webService = apiClient.createWebservice(BudgetControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetResponse> = webService.findAllByUser3(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;BudgetResponse&gt;**](BudgetResponse.md)

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
val webService = apiClient.createWebservice(BudgetControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 
val state : kotlin.String = state_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetResponse> = webService.findAllByUserAndState1(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**|  | [enum: ACTIVE, CANCELLED, FINALIZED] |

### Return type

[**kotlin.collections.List&lt;BudgetResponse&gt;**](BudgetResponse.md)

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
val webService = apiClient.createWebservice(BudgetControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : BudgetResponse = webService.findById6(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**BudgetResponse**](BudgetResponse.md)

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
val webService = apiClient.createWebservice(BudgetControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val budgetRequest : BudgetRequest =  // BudgetRequest | 

launch(Dispatchers.IO) {
    val result : BudgetResponse = webService.update6(id, budgetRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **budgetRequest** | [**BudgetRequest**](BudgetRequest.md)|  | |

### Return type

[**BudgetResponse**](BudgetResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

