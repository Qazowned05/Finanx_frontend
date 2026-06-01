# ScheduleControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**create1**](ScheduleControllerApi.md#create1) | **POST** schedule |  |
| [**findById2**](ScheduleControllerApi.md#findById2) | **GET** schedule/{id} |  |
| [**update2**](ScheduleControllerApi.md#update2) | **PUT** schedule/{id} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleControllerApi::class.java)
val update2Request : Update2Request =  // Update2Request | 

launch(Dispatchers.IO) {
    val result : FindById2200Response = webService.create1(update2Request)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **update2Request** | [**Update2Request**](Update2Request.md)|  | |

### Return type

[**FindById2200Response**](FindById2200Response.md)

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
val webService = apiClient.createWebservice(ScheduleControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : FindById2200Response = webService.findById2(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**FindById2200Response**](FindById2200Response.md)

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
val webService = apiClient.createWebservice(ScheduleControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val update2Request : Update2Request =  // Update2Request | 

launch(Dispatchers.IO) {
    val result : FindById2200Response = webService.update2(id, update2Request)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **update2Request** | [**Update2Request**](Update2Request.md)|  | |

### Return type

[**FindById2200Response**](FindById2200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

