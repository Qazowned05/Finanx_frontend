# SavingsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelSavingGoal**](SavingsApi.md#cancelSavingGoal) | **POST** savings/{id}/cancel | Cancelar meta |
| [**createSavingGoal**](SavingsApi.md#createSavingGoal) | **POST** savings | Crear meta |
| [**disableSavingGoal**](SavingsApi.md#disableSavingGoal) | **POST** savings/{id}/disable | Deshabilitar meta |
| [**findAllActiveSavingGoalByUser**](SavingsApi.md#findAllActiveSavingGoalByUser) | **GET** savings/user/{idUser} | Listar metas activas |
| [**findSavingGoalById**](SavingsApi.md#findSavingGoalById) | **GET** savings/{id} | Obtener una meta de ahorro |
| [**updateSavingGoal**](SavingsApi.md#updateSavingGoal) | **PUT** savings/{id} | Actualizar meta |



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


Listar metas activas

Obtiene metas de ahorro activas por usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(SavingsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID usuario

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<SavingGoalResponse> = webService.findAllActiveSavingGoalByUser(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**| ID usuario | |

### Return type

[**kotlin.collections.List&lt;SavingGoalResponse&gt;**](SavingGoalResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Obtener una meta de ahorro

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

