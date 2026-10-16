# AnalyticsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getBalanceDashboard**](AnalyticsApi.md#getBalanceDashboard) | **POST** api/v1/analytics/dashboard/balance | Dashboard de balance |
| [**getBudgetExecution**](AnalyticsApi.md#getBudgetExecution) | **POST** api/v1/analytics/budgets/{id}/execution | Ejecucion de presupuesto |
| [**getBudgetsDashboard**](AnalyticsApi.md#getBudgetsDashboard) | **POST** api/v1/analytics/dashboard/budgets | Dashboard de presupuestos |
| [**getBudgetsExecutionsByPeriod**](AnalyticsApi.md#getBudgetsExecutionsByPeriod) | **GET** api/v1/analytics/budgets/executions | Ejecuciones por periodo |



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

launch(Dispatchers.IO) {
    val result : TransactionsDashboard = webService.getBalanceDashboard(periodRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |

### Return type

[**TransactionsDashboard**](TransactionsDashboard.md)

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

launch(Dispatchers.IO) {
    val result : BudgetsDashboard = webService.getBudgetsDashboard(periodRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |

### Return type

[**BudgetsDashboard**](BudgetsDashboard.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Ejecuciones por periodo

Obtiene ejecuciones de presupuestos paginadas por un rango de fechas del usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsApi::class.java)
val from : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val to : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseBudgetExecutionResponse = webService.getBudgetsExecutionsByPeriod(from, to, page, size, sort)
}
```

### Parameters
| **from** | **java.time.LocalDate**|  | |
| **to** | **java.time.LocalDate**|  | |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;createdAt,DESC&quot;)] |

### Return type

[**PageResponseBudgetExecutionResponse**](PageResponseBudgetExecutionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

