# NotificationsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**discardNotification**](NotificationsApi.md#discardNotification) | **POST** api/v1/notifications/discard | Descartar notificacion |
| [**findNotificationById**](NotificationsApi.md#findNotificationById) | **GET** api/v1/notifications/{id} | Buscar notificacion |
| [**listNotifications**](NotificationsApi.md#listNotifications) | **GET** api/v1/notifications | Listar notificaciones del usuario |
| [**markAsRead**](NotificationsApi.md#markAsRead) | **POST** api/v1/notifications/mark-as-read | Marcar como leida |
| [**purgeNotifications**](NotificationsApi.md#purgeNotifications) | **POST** api/v1/notifications/purge-older-than | Eliminar notificaciones antiguas |



Descartar notificacion

Marca una notificacion como descartada.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationsApi::class.java)
val body : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.discardNotification(body)
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


Buscar notificacion

Obtiene una notificacion por ID.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID notificacion

launch(Dispatchers.IO) {
    val result : NotificationDTO = webService.findNotificationById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID notificacion | |

### Return type

[**NotificationDTO**](NotificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar notificaciones del usuario

Lista paginada de notificaciones del usuario autenticado con filtros opcionales state y search.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationsApi::class.java)
val state : NotificationState =  // NotificationState | 
val search : kotlin.String = search_example // kotlin.String | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseNotificationDTO = webService.listNotifications(state, search, page, size, sort)
}
```

### Parameters
| **state** | [**NotificationState**](.md)|  | [optional] [enum: SCHEDULED, SENT, READ, DISCARD] |
| **search** | **kotlin.String**|  | [optional] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;createdAt,DESC&quot;)] |

### Return type

[**PageResponseNotificationDTO**](PageResponseNotificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Marcar como leida

Marca una notificacion como leida.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationsApi::class.java)
val body : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.markAsRead(body)
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


Eliminar notificaciones antiguas

Elimina notificaciones anteriores a una fecha.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationsApi::class.java)
val dateTime : java.time.LocalDateTime = 2013-10-20T19:20:30+01:00 // java.time.LocalDateTime | Fecha limite

launch(Dispatchers.IO) {
    webService.purgeNotifications(dateTime)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **dateTime** | **java.time.LocalDateTime**| Fecha limite | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

