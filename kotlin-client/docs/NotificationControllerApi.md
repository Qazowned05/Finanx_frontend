# NotificationControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**discard**](NotificationControllerApi.md#discard) | **POST** notifications/discard |  |
| [**findAllByUser1**](NotificationControllerApi.md#findAllByUser1) | **GET** notifications/{idUser} |  |
| [**findAllByUserAndState**](NotificationControllerApi.md#findAllByUserAndState) | **GET** notifications/{idUser}/{state} |  |
| [**findById7**](NotificationControllerApi.md#findById7) | **GET** notifications/{id} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationControllerApi::class.java)
val body : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.discard(body)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **body** | **kotlin.Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<NotificationDTO> = webService.findAllByUser1(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;NotificationDTO&gt;**](NotificationDTO.md)

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
val webService = apiClient.createWebservice(NotificationControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 
val state : kotlin.String = state_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<NotificationDTO> = webService.findAllByUserAndState(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**|  | [enum: SCHEDULED, SENT, READ, DISCARD] |

### Return type

[**kotlin.collections.List&lt;NotificationDTO&gt;**](NotificationDTO.md)

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
val webService = apiClient.createWebservice(NotificationControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : NotificationDTO = webService.findById7(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**NotificationDTO**](NotificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

