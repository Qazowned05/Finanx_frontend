# TransactionScheduleControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**findAllByUserAndState1**](TransactionScheduleControllerApi.md#findAllByUserAndState1) | **GET** schedule/transactions/user/{idUser}/{state} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionScheduleControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 
val state : kotlin.String = state_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionScheduleResponse> = webService.findAllByUserAndState1(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**|  | [enum: RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR] |

### Return type

[**kotlin.collections.List&lt;TransactionScheduleResponse&gt;**](TransactionScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

