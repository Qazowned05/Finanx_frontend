# ScheduleApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelSchedule**](ScheduleApi.md#cancelSchedule) | **POST** schedule/{id}/cancel | Cancelar programacion |
| [**createSchedule**](ScheduleApi.md#createSchedule) | **POST** schedule | Crear programacion |
| [**disableSchedule**](ScheduleApi.md#disableSchedule) | **DELETE** schedule/{id}/disable | Deshabilitar programacion |
| [**findAllScheduleByUserAndState**](ScheduleApi.md#findAllScheduleByUserAndState) | **GET** schedule/user/{idUser}/{state} | Listar programaciones por usuario y estado |
| [**pauseSchedule**](ScheduleApi.md#pauseSchedule) | **POST** schedule/{id}/pause | Pausar programacion |
| [**resumeSchedule**](ScheduleApi.md#resumeSchedule) | **POST** schedule/{id}/resume | Reanudar programacion |
| [**updateSchedule**](ScheduleApi.md#updateSchedule) | **PUT** schedule/{id} | Actualizar programacion |



Cancelar programacion

Cancela una programacion.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la programacion

launch(Dispatchers.IO) {
    webService.cancelSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la programacion | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Crear programacion

Crea una nueva programacion.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val createScheduleRequest : CreateScheduleRequest =  // CreateScheduleRequest | 

launch(Dispatchers.IO) {
    val result : UpdateSchedule200Response = webService.createSchedule(createScheduleRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **createScheduleRequest** | [**CreateScheduleRequest**](CreateScheduleRequest.md)|  | |

### Return type

[**UpdateSchedule200Response**](UpdateSchedule200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Deshabilitar programacion

Deshabilita una programacion.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la programacion

launch(Dispatchers.IO) {
    webService.disableSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la programacion | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Listar programaciones por usuario y estado

Obtiene programaciones activas filtradas por usuario y estado.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID usuario
val state : kotlin.String = state_example // kotlin.String | Estado de la programacion

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<UpdateSchedule200Response> = webService.findAllScheduleByUserAndState(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**| ID usuario | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**| Estado de la programacion | [enum: RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR] |

### Return type

[**kotlin.collections.List&lt;UpdateSchedule200Response&gt;**](UpdateSchedule200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Pausar programacion

Pausa una programacion.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la programacion

launch(Dispatchers.IO) {
    webService.pauseSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la programacion | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Reanudar programacion

Reanuda una programacion pausada.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la programacion

launch(Dispatchers.IO) {
    webService.resumeSchedule(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la programacion | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Actualizar programacion

Actualiza una programacion existente.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la programacion
val updateScheduleRequest : UpdateScheduleRequest =  // UpdateScheduleRequest | 

launch(Dispatchers.IO) {
    val result : UpdateSchedule200Response = webService.updateSchedule(id, updateScheduleRequest)
}
```

### Parameters
| **id** | **kotlin.Long**| ID de la programacion | |
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

