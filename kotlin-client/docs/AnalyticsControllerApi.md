# AnalyticsControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**balance**](AnalyticsControllerApi.md#balance) | **GET** analytics/dashboard/balance |  |
| [**budgets**](AnalyticsControllerApi.md#budgets) | **GET** analytics/dashboard/budgets |  |
| [**getExecution**](AnalyticsControllerApi.md#getExecution) | **GET** analytics/budgets/{id}/execution |  |
| [**getExecutionsByPeriod**](AnalyticsControllerApi.md#getExecutionsByPeriod) | **GET** analytics/budgets/executions |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val periodRequest : PeriodRequest =  // PeriodRequest | 
val prompt : kotlin.String = prompt_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : Budgets200Response = webService.balance(periodRequest, prompt)
}
```

### Parameters
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **prompt** | **kotlin.String**|  | [optional] |

### Return type

[**Budgets200Response**](Budgets200Response.md)

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
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val periodRequest : PeriodRequest =  // PeriodRequest | 
val prompt : kotlin.String = prompt_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : Budgets200Response = webService.budgets(periodRequest, prompt)
}
```

### Parameters
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **prompt** | **kotlin.String**|  | [optional] |

### Return type

[**Budgets200Response**](Budgets200Response.md)

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
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : BudgetExecutionResponse = webService.getExecution(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**BudgetExecutionResponse**](BudgetExecutionResponse.md)

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
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val request : PeriodRequest =  // PeriodRequest | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetExecutionResponse> = webService.getExecutionsByPeriod(request)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **request** | [**PeriodRequest**](.md)|  | |

### Return type

[**kotlin.collections.List&lt;BudgetExecutionResponse&gt;**](BudgetExecutionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

