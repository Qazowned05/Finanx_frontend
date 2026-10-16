# ReasonsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createReason**](ReasonsApi.md#createReason) | **POST** api/v1/reasons | Crear razon |
| [**disableReason**](ReasonsApi.md#disableReason) | **POST** api/v1/reasons/{id}/disable | Deshabilitar razon |
| [**findReasonById**](ReasonsApi.md#findReasonById) | **GET** api/v1/reasons/{id} | Buscar razon |
| [**listReasons**](ReasonsApi.md#listReasons) | **GET** api/v1/reasons | Listar razones del usuario |
| [**updateReason**](ReasonsApi.md#updateReason) | **PUT** api/v1/reasons/{id} | Actualizar razon |



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


Listar razones del usuario

Lista paginada de razones del usuario autenticado con filtros active y search; active&#x3D;true por defecto.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReasonsApi::class.java)
val active : kotlin.Boolean = true // kotlin.Boolean | 
val search : kotlin.String = search_example // kotlin.String | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseReasonResponse = webService.listReasons(active, search, page, size, sort)
}
```

### Parameters
| **active** | **kotlin.Boolean**|  | [optional] [default to true] |
| **search** | **kotlin.String**|  | [optional] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;description,ASC&quot;)] |

### Return type

[**PageResponseReasonResponse**](PageResponseReasonResponse.md)

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

