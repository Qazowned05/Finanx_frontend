# AnalyticsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getBalanceDashboard**](AnalyticsApi.md#getBalanceDashboard) | **POST** analytics/dashboard/balance | Dashboard de balance |
| [**getBudgetExecution**](AnalyticsApi.md#getBudgetExecution) | **POST** analytics/budgets/{id}/execution | Ejecucion de presupuesto |
| [**getBudgetsDashboard**](AnalyticsApi.md#getBudgetsDashboard) | **POST** analytics/dashboard/budgets | Dashboard de presupuestos |
| [**getBudgetsExecutionsByPeriod**](AnalyticsApi.md#getBudgetsExecutionsByPeriod) | **POST** analytics/budgets/executions | Ejecuciones por periodo |



Dashboard de balance

Genera el dashboard de balance basado en un periodo y un prompt opcional.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsApi::class.java)
val periodRequest : PeriodRequest =  // PeriodRequest | 
val prompt : kotlin.String = prompt_example // kotlin.String | Prompt opcional

launch(Dispatchers.IO) {
    val result : GetBudgetsDashboard200Response = webService.getBalanceDashboard(periodRequest, prompt)
}
```

### Parameters
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **prompt** | **kotlin.String**| Prompt opcional | [optional] |

### Return type

[**GetBudgetsDashboard200Response**](GetBudgetsDashboard200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Ejecucion de presupuesto

Obtiene la ejecucion de un presupuesto por ID.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID presupuesto

launch(Dispatchers.IO) {
    val result : BudgetExecutionResponse = webService.getBudgetExecution(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID presupuesto | |

### Return type

[**BudgetExecutionResponse**](BudgetExecutionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Dashboard de presupuestos

Genera el dashboard de presupuestos.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsApi::class.java)
val periodRequest : PeriodRequest =  // PeriodRequest | 
val prompt : kotlin.String = prompt_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : GetBudgetsDashboard200Response = webService.getBudgetsDashboard(periodRequest, prompt)
}
```

### Parameters
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **prompt** | **kotlin.String**|  | [optional] |

### Return type

[**GetBudgetsDashboard200Response**](GetBudgetsDashboard200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Ejecuciones por periodo

Obtiene ejecuciones de presupuestos por periodo.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsApi::class.java)
val periodRequest : PeriodRequest =  // PeriodRequest | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetExecutionResponse> = webService.getBudgetsExecutionsByPeriod(periodRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |

### Return type

[**kotlin.collections.List&lt;BudgetExecutionResponse&gt;**](BudgetExecutionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

