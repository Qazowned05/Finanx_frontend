# ScheduleApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelSchedule**](ScheduleApi.md#cancelSchedule) | **POST** api/v1/schedule/{id}/cancel | Cancelar programacion |
| [**createSchedule**](ScheduleApi.md#createSchedule) | **POST** api/v1/schedule | Crear programacion |
| [**disableSchedule**](ScheduleApi.md#disableSchedule) | **DELETE** api/v1/schedule/{id}/disable | Deshabilitar programacion |
| [**listSchedules**](ScheduleApi.md#listSchedules) | **GET** api/v1/schedule | Listar programaciones por usuario y estado |
| [**pauseSchedule**](ScheduleApi.md#pauseSchedule) | **POST** api/v1/schedule/{id}/pause | Pausar programacion |
| [**resumeSchedule**](ScheduleApi.md#resumeSchedule) | **POST** api/v1/schedule/{id}/resume | Reanudar programacion |
| [**updateSchedule**](ScheduleApi.md#updateSchedule) | **PUT** api/v1/schedule/{id} | Actualizar programacion |



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
val scheduleRequest : ScheduleRequest =  // ScheduleRequest | 

launch(Dispatchers.IO) {
    val result : ScheduleResponse = webService.createSchedule(scheduleRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **scheduleRequest** | [**ScheduleRequest**](ScheduleRequest.md)|  | |

### Return type

[**ScheduleResponse**](ScheduleResponse.md)

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

Lista programaciones activas paginadas con filtro opcional state.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ScheduleApi::class.java)
val state : ScheduleState =  // ScheduleState | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseScheduleResponse = webService.listSchedules(state, page, size, sort)
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

[**PageResponseScheduleResponse**](PageResponseScheduleResponse.md)

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
val scheduleRequest : ScheduleRequest =  // ScheduleRequest | 

launch(Dispatchers.IO) {
    val result : ScheduleResponse = webService.updateSchedule(id, scheduleRequest)
}
```

### Parameters
| **id** | **kotlin.Long**| ID de la programacion | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **scheduleRequest** | [**ScheduleRequest**](ScheduleRequest.md)|  | |

### Return type

[**ScheduleResponse**](ScheduleResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

