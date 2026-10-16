# ChatBotApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**closeSession**](ChatBotApi.md#closeSession) | **PATCH** api/v1/chat/sessions/{sessionId}/close | Cerrar sesion de chat |
| [**findSessionById**](ChatBotApi.md#findSessionById) | **GET** api/v1/chat/sessions/{sessionId} | Obtener sesion por ID |
| [**findSessionByUUID**](ChatBotApi.md#findSessionByUUID) | **GET** api/v1/chat/sessions/uuid/{uuid} | Obtener sesion por UUID |
| [**listChatSessions**](ChatBotApi.md#listChatSessions) | **GET** api/v1/chat/sessions | Listar sesiones del usuario |
| [**sendMessage**](ChatBotApi.md#sendMessage) | **POST** api/v1/chat/sessions/{sessionId}/messages | Enviar mensaje al chatbot |
| [**startSession**](ChatBotApi.md#startSession) | **POST** api/v1/chat/sessions | Iniciar sesion de chat |



Cerrar sesion de chat

Marca la sesion como cerrada. No se podran enviar mas mensajes en ella.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatBotApi::class.java)
val sessionId : kotlin.Long = 1 // kotlin.Long | ID de la sesion

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.closeSession(sessionId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sessionId** | **kotlin.Long**| ID de la sesion | |

### Return type

[**ChatSessionResponse**](ChatSessionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Obtener sesion por ID

Retorna el detalle de una sesion incluyendo su historial de mensajes.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatBotApi::class.java)
val sessionId : kotlin.Long = 1 // kotlin.Long | ID de la sesion

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.findSessionById(sessionId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sessionId** | **kotlin.Long**| ID de la sesion | |

### Return type

[**ChatSessionResponse**](ChatSessionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Obtener sesion por UUID

Retorna el detalle de una sesion usando su UUID publico.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatBotApi::class.java)
val uuid : kotlin.String = 550e8400-e29b-41d4-a716-446655440000 // kotlin.String | UUID de la sesion

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.findSessionByUUID(uuid)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **uuid** | **kotlin.String**| UUID de la sesion | |

### Return type

[**ChatSessionResponse**](ChatSessionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar sesiones del usuario

Retorna sesiones de chat paginadas; admite page, size y sort estándar.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatBotApi::class.java)
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseChatSessionResponse = webService.listChatSessions(page, size, sort)
}
```

### Parameters
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;createdAt,DESC&quot;)] |

### Return type

[**PageResponseChatSessionResponse**](PageResponseChatSessionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Enviar mensaje al chatbot

Envia un mensaje dentro de una sesion activa. El chatbot detecta automaticamente la skill apropiada (ADVISOR para consultas financieras, CHITCHAT para conversacion general) e inyecta el contexto financiero cuando corresponde. Retorna la respuesta del asistente lista para mostrar al usuario. 

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatBotApi::class.java)
val sessionId : kotlin.Long = 1 // kotlin.Long | ID de la sesion activa
val sendMessageRequest : SendMessageRequest =  // SendMessageRequest | 

launch(Dispatchers.IO) {
    val result : ChatHistoryResponse = webService.sendMessage(sessionId, sendMessageRequest)
}
```

### Parameters
| **sessionId** | **kotlin.Long**| ID de la sesion activa | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sendMessageRequest** | [**SendMessageRequest**](SendMessageRequest.md)|  | |

### Return type

[**ChatHistoryResponse**](ChatHistoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Iniciar sesion de chat

Crea una nueva sesion de chat e inyecta el contexto financiero del periodo actual y anterior.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatBotApi::class.java)

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.startSession()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**ChatSessionResponse**](ChatSessionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

