# ScheduleControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelSchedule**](ScheduleControllerApi.md#cancelSchedule) | **POST** schedule/{id}/cancel |  |
| [**createSchedule**](ScheduleControllerApi.md#createSchedule) | **POST** schedule |  |
| [**disableSchedule**](ScheduleControllerApi.md#disableSchedule) | **POST** schedule/{id}/disable |  |
| [**findSchedulesByUserAndState**](ScheduleControllerApi.md#findSchedulesByUserAndState) | **GET** schedule/user/{idUser}/{state} |  |
| [**pauseSchedule**](ScheduleControllerApi.md#pauseSchedule) | **POST** schedule/{id}/pause |  |
| [**resumeSchedule**](ScheduleControllerApi.md#resumeSchedule) | **POST** schedule/{id}/resume |  |
| [**updateSchedule**](ScheduleControllerApi.md#updateSchedule) | **PUT** schedule/{id} |  |





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
    webService.cancelSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleControllerApi::class.java)
val updateScheduleRequest : UpdateScheduleRequest =  // UpdateScheduleRequest | 

launch(Dispatchers.IO) {
    val result : UpdateSchedule200Response = webService.createSchedule(updateScheduleRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateScheduleRequest** | [**UpdateScheduleRequest**](UpdateScheduleRequest.md)|  | |

### Return type

[**UpdateSchedule200Response**](UpdateSchedule200Response.md)

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
    webService.disableSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 
val state : kotlin.String = state_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<UpdateSchedule200Response> = webService.findSchedulesByUserAndState(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**|  | [enum: RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR] |

### Return type

[**kotlin.collections.List&lt;UpdateSchedule200Response&gt;**](UpdateSchedule200Response.md)

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

launch(Dispatchers.IO) {
    webService.pauseSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




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
    webService.resumeSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val updateScheduleRequest : UpdateScheduleRequest =  // UpdateScheduleRequest | 

launch(Dispatchers.IO) {
    val result : UpdateSchedule200Response = webService.updateSchedule(id, updateScheduleRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **updateScheduleRequest** | [**UpdateScheduleRequest**](UpdateScheduleRequest.md)|  | |

### Return type

[**UpdateSchedule200Response**](UpdateSchedule200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

