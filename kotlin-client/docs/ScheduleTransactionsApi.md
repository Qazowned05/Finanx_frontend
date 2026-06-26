# ScheduleTransactionsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**findAllTScheduleByUserAndState**](ScheduleTransactionsApi.md#findAllTScheduleByUserAndState) | **GET** schedule/transactions/user/{idUser}/{state} | Listar programaciones de transacciones |



Listar programaciones de transacciones

Obtiene programaciones de transacciones por usuario y estado.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleTransactionsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID usuario
val state : kotlin.String = state_example // kotlin.String | Estado

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionScheduleResponse> = webService.findAllTScheduleByUserAndState(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**| ID usuario | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**| Estado | [enum: RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR] |

### Return type

[**kotlin.collections.List&lt;TransactionScheduleResponse&gt;**](TransactionScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

