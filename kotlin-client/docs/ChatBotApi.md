# ChatBotApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**closeSession**](ChatBotApi.md#closeSession) | **PATCH** users/{userId}/chat/sessions/{sessionId}/close | Cerrar sesion de chat |
| [**findAllSessionsByUser**](ChatBotApi.md#findAllSessionsByUser) | **GET** users/{userId}/chat/sessions | Listar sesiones del usuario |
| [**findSessionById**](ChatBotApi.md#findSessionById) | **GET** users/{userId}/chat/sessions/{sessionId} | Obtener sesion por ID |
| [**findSessionByUUID**](ChatBotApi.md#findSessionByUUID) | **GET** users/{userId}/chat/sessions/uuid/{uuid} | Obtener sesion por UUID |
| [**sendMessage**](ChatBotApi.md#sendMessage) | **POST** users/{userId}/chat/sessions/{sessionId}/messages | Enviar mensaje al chatbot |
| [**startSession**](ChatBotApi.md#startSession) | **POST** users/{userId}/chat/sessions | Iniciar sesion de chat |



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
val userId : kotlin.Long = 1 // kotlin.Long | ID del usuario
val sessionId : kotlin.Long = 1 // kotlin.Long | ID de la sesion

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.closeSession(userId, sessionId)
}
```

### Parameters
| **userId** | **kotlin.Long**| ID del usuario | |
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


Listar sesiones del usuario

Retorna todas las sesiones de chat del usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ChatBotApi::class.java)
val userId : kotlin.Long = 1 // kotlin.Long | ID del usuario

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<ChatSessionResponse> = webService.findAllSessionsByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**| ID del usuario | |

### Return type

[**kotlin.collections.List&lt;ChatSessionResponse&gt;**](ChatSessionResponse.md)

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
val userId : kotlin.Long = 1 // kotlin.Long | ID del usuario
val sessionId : kotlin.Long = 1 // kotlin.Long | ID de la sesion

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.findSessionById(userId, sessionId)
}
```

### Parameters
| **userId** | **kotlin.Long**| ID del usuario | |
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
val userId : kotlin.Long = 1 // kotlin.Long | ID del usuario
val uuid : kotlin.String = 550e8400-e29b-41d4-a716-446655440000 // kotlin.String | UUID de la sesion

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.findSessionByUUID(userId, uuid)
}
```

### Parameters
| **userId** | **kotlin.Long**| ID del usuario | |
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
val userId : kotlin.Long = 1 // kotlin.Long | ID del usuario
val sessionId : kotlin.Long = 1 // kotlin.Long | ID de la sesion activa
val sendMessageRequest : SendMessageRequest =  // SendMessageRequest | 

launch(Dispatchers.IO) {
    val result : ChatHistoryResponse = webService.sendMessage(userId, sessionId, sendMessageRequest)
}
```

### Parameters
| **userId** | **kotlin.Long**| ID del usuario | |
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
val userId : kotlin.Long = 1 // kotlin.Long | ID del usuario

launch(Dispatchers.IO) {
    val result : ChatSessionResponse = webService.startSession(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**| ID del usuario | |

### Return type

[**ChatSessionResponse**](ChatSessionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

