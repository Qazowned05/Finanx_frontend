# BudgetsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelBudget**](BudgetsApi.md#cancelBudget) | **POST** budgets/{id}/cancel | Cancelar presupuesto |
| [**createBudget**](BudgetsApi.md#createBudget) | **POST** budgets | Crear presupuesto |
| [**disableBudget**](BudgetsApi.md#disableBudget) | **POST** budgets/{id}/disable | Deshabilitar presupuesto |
| [**findAllBudgetsByPeriodAndHealth**](BudgetsApi.md#findAllBudgetsByPeriodAndHealth) | **POST** budgets/{health} | Filtrar por salud |
| [**findAllBudgetsByUser**](BudgetsApi.md#findAllBudgetsByUser) | **GET** budgets/user/{idUser} | Listar presupuestos por usuario |
| [**findAllBudgetsByUserAndState**](BudgetsApi.md#findAllBudgetsByUserAndState) | **GET** budgets/user/{idUser}/{state} | Listar por usuario y estado |
| [**findBudgetById**](BudgetsApi.md#findBudgetById) | **GET** budgets/{id} | Buscar presupuesto |
| [**updateBudget**](BudgetsApi.md#updateBudget) | **PUT** budgets/{id} | Actualizar presupuesto |



Cancelar presupuesto

Cancela un presupuesto.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.cancelBudget(id)
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


Crear presupuesto

Crea un nuevo presupuesto.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val budgetRequest : BudgetRequest =  // BudgetRequest | 

launch(Dispatchers.IO) {
    val result : BudgetResponse = webService.createBudget(budgetRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **budgetRequest** | [**BudgetRequest**](BudgetRequest.md)|  | |

### Return type

[**BudgetResponse**](BudgetResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Deshabilitar presupuesto

Deshabilita un presupuesto.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.disableBudget(id)
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


Filtrar por salud

Filtra presupuestos por health.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val health : kotlin.String = health_example // kotlin.String | 
val periodRequest : PeriodRequest =  // PeriodRequest | 

launch(Dispatchers.IO) {
    val result : TimeBoundListBudgetResponse = webService.findAllBudgetsByPeriodAndHealth(health, periodRequest)
}
```

### Parameters
| **health** | **kotlin.String**|  | [enum: OK, NEAR_LIMIT, EXCEEDED, INACTIVE] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **periodRequest** | [**PeriodRequest**](PeriodRequest.md)|  | |

### Return type

[**TimeBoundListBudgetResponse**](TimeBoundListBudgetResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Listar presupuestos por usuario

Obtiene todos los presupuestos de un usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID usuario

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetResponse> = webService.findAllBudgetsByUser(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**| ID usuario | |

### Return type

[**kotlin.collections.List&lt;BudgetResponse&gt;**](BudgetResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar por usuario y estado

Obtiene presupuestos filtrados por estado.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 
val state : kotlin.String = state_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetResponse> = webService.findAllBudgetsByUserAndState(idUser, state)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **state** | **kotlin.String**|  | [enum: ACTIVE, CANCELLED, DISABLED, FINALIZED] |

### Return type

[**kotlin.collections.List&lt;BudgetResponse&gt;**](BudgetResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Buscar presupuesto

Obtiene un presupuesto por ID.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : BudgetResponse = webService.findBudgetById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**BudgetResponse**](BudgetResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Actualizar presupuesto

Actualiza un presupuesto.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val budgetRequest : BudgetRequest =  // BudgetRequest | 

launch(Dispatchers.IO) {
    val result : BudgetResponse = webService.updateBudget(id, budgetRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **budgetRequest** | [**BudgetRequest**](BudgetRequest.md)|  | |

### Return type

[**BudgetResponse**](BudgetResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

