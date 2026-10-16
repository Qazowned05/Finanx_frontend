# SavingsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelSavingGoal**](SavingsApi.md#cancelSavingGoal) | **POST** api/v1/savings/{id}/cancel | Cancelar meta |
| [**createSavingGoal**](SavingsApi.md#createSavingGoal) | **POST** api/v1/savings | Crear meta |
| [**disableSavingGoal**](SavingsApi.md#disableSavingGoal) | **POST** api/v1/savings/{id}/disable | Deshabilitar meta |
| [**findSavingGoalById**](SavingsApi.md#findSavingGoalById) | **GET** api/v1/savings/{id} | Buscar meta |
| [**listSavingGoals**](SavingsApi.md#listSavingGoals) | **GET** api/v1/savings | Listar metas del usuario |
| [**updateSavingGoal**](SavingsApi.md#updateSavingGoal) | **PUT** api/v1/savings/{id} | Actualizar meta |



Cancelar meta

Cancela una meta de ahorro.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.cancelSavingGoal(id)
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


Crear meta

Crea una nueva meta de ahorro.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingsApi::class.java)
val savingGoalRequest : SavingGoalRequest =  // SavingGoalRequest | 

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.createSavingGoal(savingGoalRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **savingGoalRequest** | [**SavingGoalRequest**](SavingGoalRequest.md)|  | |

### Return type

[**SavingGoalResponse**](SavingGoalResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Deshabilitar meta

Deshabilita una meta.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.disableSavingGoal(id)
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


Buscar meta

Obtiene una meta de ahorro por ID.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID meta

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.findSavingGoalById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID meta | |

### Return type

[**SavingGoalResponse**](SavingGoalResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar metas del usuario

Obtiene las metas de ahorro del usuario autenticado.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingsApi::class.java)
val active : kotlin.Boolean = true // kotlin.Boolean | 
val state : SavingGoalState =  // SavingGoalState | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseSavingGoalResponse = webService.listSavingGoals(active, state, page, size, sort)
}
```

### Parameters
| **active** | **kotlin.Boolean**|  | [optional] [default to true] |
| **state** | [**SavingGoalState**](.md)|  | [optional] [enum: RUNNING, COMPLETED, CANCELLED] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;deadline,ASC&quot;)] |

### Return type

[**PageResponseSavingGoalResponse**](PageResponseSavingGoalResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Actualizar meta

Actualiza una meta de ahorro.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val savingGoalRequest : SavingGoalRequest =  // SavingGoalRequest | 

launch(Dispatchers.IO) {
    val result : SavingGoalResponse = webService.updateSavingGoal(id, savingGoalRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **savingGoalRequest** | [**SavingGoalRequest**](SavingGoalRequest.md)|  | |

### Return type

[**SavingGoalResponse**](SavingGoalResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

