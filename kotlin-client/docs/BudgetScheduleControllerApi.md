# BudgetScheduleControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**findAllByUserAndState2**](BudgetScheduleControllerApi.md#findAllByUserAndState2) | **GET** schedule/budgets/{idUser}/{state} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetScheduleControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 
val state : kotlin.String = state_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetScheduleResponse> = webService.findAllByUserAndState2(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**|  | [enum: RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR] |

### Return type

[**kotlin.collections.List&lt;BudgetScheduleResponse&gt;**](BudgetScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

