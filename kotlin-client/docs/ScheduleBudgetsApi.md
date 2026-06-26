# ScheduleBudgetsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**findAllBudgetScheduleByUserAndState**](ScheduleBudgetsApi.md#findAllBudgetScheduleByUserAndState) | **GET** schedule/budgets/user/{idUser}/{state} | Listar programaciones de presupuestos |



Listar programaciones de presupuestos

Obtiene programaciones de presupuestos por usuario y estado.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleBudgetsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID usuario
val state : kotlin.String = state_example // kotlin.String | Estado

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetScheduleResponse> = webService.findAllBudgetScheduleByUserAndState(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**| ID usuario | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**| Estado | [enum: RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR] |

### Return type

[**kotlin.collections.List&lt;BudgetScheduleResponse&gt;**](BudgetScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

