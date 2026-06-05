# ReportControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**balance**](ReportControllerApi.md#balance) | **POST** reports/balance |  |
| [**exportBalance**](ReportControllerApi.md#exportBalance) | **POST** reports/balance/export |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReportControllerApi::class.java)
val balanceReportRequest : BalanceReportRequest =  // BalanceReportRequest | 

launch(Dispatchers.IO) {
    val result : BalanceReportResponse = webService.balance(balanceReportRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **balanceReportRequest** | [**BalanceReportRequest**](BalanceReportRequest.md)|  | |

### Return type

[**BalanceReportResponse**](BalanceReportResponse.md)

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
val webService = apiClient.createWebservice(ReportControllerApi::class.java)
val fileType : kotlin.String = fileType_example // kotlin.String | 
val balanceReportRequest : BalanceReportRequest =  // BalanceReportRequest | 

launch(Dispatchers.IO) {
    val result : kotlin.ByteArray = webService.exportBalance(fileType, balanceReportRequest)
}
```

### Parameters
| **fileType** | **kotlin.String**|  | [enum: CSV, PDF, EXCEL] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **balanceReportRequest** | [**BalanceReportRequest**](BalanceReportRequest.md)|  | |

### Return type

**kotlin.ByteArray**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/octet-stream

