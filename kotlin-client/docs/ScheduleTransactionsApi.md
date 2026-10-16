# ScheduleTransactionsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**listTransactionSchedules**](ScheduleTransactionsApi.md#listTransactionSchedules) | **GET** api/v1/schedule/transactions | Listar programaciones de transacciones |



Listar programaciones de transacciones

Lista programaciones de transacciones activas y paginadas con filtro opcional state.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleTransactionsApi::class.java)
val state : ScheduleState =  // ScheduleState | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseTransactionScheduleResponse = webService.listTransactionSchedules(state, page, size, sort)
}
```

### Parameters
| **state** | [**ScheduleState**](.md)|  | [optional] [enum: RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;nextRunAt,ASC&quot;)] |

### Return type

[**PageResponseTransactionScheduleResponse**](PageResponseTransactionScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

