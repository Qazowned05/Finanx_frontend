# BudgetControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancel2**](BudgetControllerApi.md#cancel2) | **POST** budgets/{id}/cancel |  |
| [**create5**](BudgetControllerApi.md#create5) | **POST** budgets |  |
| [**disable5**](BudgetControllerApi.md#disable5) | **POST** budgets/{id}/disable |  |
| [**findAllByUser4**](BudgetControllerApi.md#findAllByUser4) | **GET** budgets/user/{idUser} |  |
| [**findAllByUserAndState4**](BudgetControllerApi.md#findAllByUserAndState4) | **GET** budgets/user/{idUser}/{state} |  |
| [**findById5**](BudgetControllerApi.md#findById5) | **GET** budgets/{id} |  |
| [**getBudgetsByHealth**](BudgetControllerApi.md#getBudgetsByHealth) | **POST** budgets/{health} |  |
| [**update6**](BudgetControllerApi.md#update6) | **PUT** budgets/{id} |  |





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
    webService.cancel2(id)
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
    val result : kotlin.collections.List<BudgetResponse> = webService.findAllByUser4(idUser)
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
    val result : kotlin.collections.List<BudgetResponse> = webService.findAllByUserAndState4(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**|  | [enum: ACTIVE, CANCELLED, DISABLED, FINALIZED] |

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
    val result : BudgetResponse = webService.findById5(id)
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
val health : kotlin.String = health_example // kotlin.String | 
val periodRequest : PeriodRequest =  // PeriodRequest | 

launch(Dispatchers.IO) {
    val result : TimeBoundListBudgetResponse = webService.getBudgetsByHealth(health, periodRequest)
}
```

### Parameters
| **health** | **kotlin.String**|  | [enum: OK, NEAR_LIMIT, EXCEEDED, INACTIVE] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |

### Return type

[**TimeBoundListBudgetResponse**](TimeBoundListBudgetResponse.md)

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

