# NotificationsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**discardNotification**](NotificationsApi.md#discardNotification) | **POST** notifications/discard | Descartar notificacion |
| [**findAllNotificationByUser**](NotificationsApi.md#findAllNotificationByUser) | **GET** notifications/user/{idUser} | Listar notificaciones por usuario |
| [**findAllNotificationsByUserAndState**](NotificationsApi.md#findAllNotificationsByUserAndState) | **GET** notifications/user/{idUser}/{state} | Listar por estado |
| [**findNotificationById**](NotificationsApi.md#findNotificationById) | **GET** notifications/{id} | Buscar notificacion |
| [**markAsRead**](NotificationsApi.md#markAsRead) | **POST** notifications/mark-as-read | Marcar como leida |
| [**purgeNotifications**](NotificationsApi.md#purgeNotifications) | **POST** notifications/user/{userId}/purge-older-than | Eliminar notificaciones antiguas |



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


Listar notificaciones por usuario

Obtiene todas las notificaciones de un usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID usuario

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<NotificationDTO> = webService.findAllNotificationByUser(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**| ID usuario | |

### Return type

[**kotlin.collections.List&lt;NotificationDTO&gt;**](NotificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar por estado

Obtiene notificaciones filtradas por estado.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(NotificationsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID usuario
val state : kotlin.String = state_example // kotlin.String | Estado

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<NotificationDTO> = webService.findAllNotificationsByUserAndState(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**| ID usuario | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**| Estado | [enum: SCHEDULED, SENT, READ, DISCARD] |

### Return type

[**kotlin.collections.List&lt;NotificationDTO&gt;**](NotificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


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
val userId : kotlin.Long = 789 // kotlin.Long | ID usuario
val dateTime : java.time.LocalDateTime = 2013-10-20T19:20:30+01:00 // java.time.LocalDateTime | Fecha limite

launch(Dispatchers.IO) {
    webService.purgeNotifications(userId, dateTime)
}
```

### Parameters
| **userId** | **kotlin.Long**| ID usuario | |
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

