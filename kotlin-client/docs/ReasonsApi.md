# ReasonsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createReason**](ReasonsApi.md#createReason) | **POST** reasons | Crear razon |
| [**disableReason**](ReasonsApi.md#disableReason) | **POST** reasons/{id}/disable | Deshabilitar razon |
| [**findAllReasonByUser**](ReasonsApi.md#findAllReasonByUser) | **GET** reasons/user/{idUser} | Listar razones por usuario |
| [**findReasonById**](ReasonsApi.md#findReasonById) | **GET** reasons/{id} | Buscar razon |
| [**updateReason**](ReasonsApi.md#updateReason) | **PUT** reasons/{id} | Actualizar razon |



Crear razon

Crea una nueva razon.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReasonsApi::class.java)
val reasonRequest : ReasonRequest =  // ReasonRequest | 

launch(Dispatchers.IO) {
    val result : ReasonResponse = webService.createReason(reasonRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **reasonRequest** | [**ReasonRequest**](ReasonRequest.md)|  | |

### Return type

[**ReasonResponse**](ReasonResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Deshabilitar razon

Deshabilita una razon.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReasonsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la razon

launch(Dispatchers.IO) {
    webService.disableReason(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la razon | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Listar razones por usuario

Obtiene todas las razones asociadas a un usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReasonsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID del usuario

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<ReasonResponse> = webService.findAllReasonByUser(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**| ID del usuario | |

### Return type

[**kotlin.collections.List&lt;ReasonResponse&gt;**](ReasonResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Buscar razon

Obtiene una razon por ID.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReasonsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la razon

launch(Dispatchers.IO) {
    val result : ReasonResponse = webService.findReasonById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la razon | |

### Return type

[**ReasonResponse**](ReasonResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Actualizar razon

Actualiza una razon existente.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReasonsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la razon
val reasonRequest : ReasonRequest =  // ReasonRequest | 

launch(Dispatchers.IO) {
    val result : ReasonResponse = webService.updateReason(id, reasonRequest)
}
```

### Parameters
| **id** | **kotlin.Long**| ID de la razon | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **reasonRequest** | [**ReasonRequest**](ReasonRequest.md)|  | |

### Return type

[**ReasonResponse**](ReasonResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

