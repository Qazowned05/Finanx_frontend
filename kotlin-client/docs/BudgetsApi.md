# BudgetsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**cancelBudget**](BudgetsApi.md#cancelBudget) | **POST** api/v1/budgets/{id}/cancel | Cancelar presupuesto |
| [**createBudget**](BudgetsApi.md#createBudget) | **POST** api/v1/budgets | Crear presupuesto |
| [**disableBudget**](BudgetsApi.md#disableBudget) | **POST** api/v1/budgets/{id}/disable | Deshabilitar presupuesto |
| [**findBudgetById**](BudgetsApi.md#findBudgetById) | **GET** api/v1/budgets/{id} | Buscar presupuesto |
| [**listBudgets**](BudgetsApi.md#listBudgets) | **GET** api/v1/budgets | Listar presupuestos por usuario |
| [**updateBudget**](BudgetsApi.md#updateBudget) | **PUT** api/v1/budgets/{id} | Actualizar presupuesto |



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


Listar presupuestos por usuario

Lista paginada del usuario autenticado. Admite filtros active, state, health y rango from/to; active&#x3D;true por defecto.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(BudgetsApi::class.java)
val active : kotlin.Boolean = true // kotlin.Boolean | 
val state : BudgetState =  // BudgetState | 
val health : BudgetHealth =  // BudgetHealth | 
val from : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val to : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseBudgetResponse = webService.listBudgets(active, state, health, from, to, page, size, sort)
}
```

### Parameters
| **active** | **kotlin.Boolean**|  | [optional] [default to true] |
| **state** | [**BudgetState**](.md)|  | [optional] [enum: ACTIVE, CANCELLED, DISABLED, FINALIZED] |
| **health** | [**BudgetHealth**](.md)|  | [optional] [enum: OK, NEAR_LIMIT, EXCEEDED, INACTIVE] |
| **from** | **java.time.LocalDate**|  | [optional] |
| **to** | **java.time.LocalDate**|  | [optional] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;end,ASC&quot;)] |

### Return type

[**PageResponseBudgetResponse**](PageResponseBudgetResponse.md)

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

