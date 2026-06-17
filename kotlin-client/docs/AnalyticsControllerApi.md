# AnalyticsControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**balance**](AnalyticsControllerApi.md#balance) | **GET** analytics/dashboard/balance |  |
| [**budgets**](AnalyticsControllerApi.md#budgets) | **GET** analytics/dashboard/budgets |  |
| [**getExecution**](AnalyticsControllerApi.md#getExecution) | **GET** analytics/{id}/execution |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val periodRequest : PeriodRequest =  // PeriodRequest | 

launch(Dispatchers.IO) {
    val result : Budgets200Response = webService.balance(periodRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |

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

launch(Dispatchers.IO) {
    val result : Budgets200Response = webService.budgets(periodRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |

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

