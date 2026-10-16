# TransactionsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createTransaction**](TransactionsApi.md#createTransaction) | **POST** api/v1/transactions | Crear transaccion |
| [**findTransactionById**](TransactionsApi.md#findTransactionById) | **GET** api/v1/transactions/{id} | Buscar transaccion |
| [**listTransactions**](TransactionsApi.md#listTransactions) | **GET** api/v1/transactions | Listar transacciones del usuario |
| [**transactionDelete**](TransactionsApi.md#transactionDelete) | **DELETE** api/v1/transactions/{id} | Eliminar transaccion |
| [**updateTransaction**](TransactionsApi.md#updateTransaction) | **PUT** api/v1/transactions/{id} | Actualizar transaccion |



Crear transaccion

Crea una nueva transaccion.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val transactionRequest : TransactionRequest =  // TransactionRequest | 

launch(Dispatchers.IO) {
    val result : TransactionResponse = webService.createTransaction(transactionRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **transactionRequest** | [**TransactionRequest**](TransactionRequest.md)|  | |

### Return type

[**TransactionResponse**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Buscar transaccion

Obtiene una transaccion por ID.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : TransactionResponse = webService.findTransactionById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**TransactionResponse**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar transacciones del usuario

Lista paginada del usuario autenticado. Admite filtros por active, type, walletId, categoryId, rango from/to y busqueda de texto libre sobre el motivo y la categoria; el orden usa sort estándar.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val active : kotlin.Boolean = true // kotlin.Boolean | 
val type : TransactionType =  // TransactionType | 
val walletId : kotlin.Long = 789 // kotlin.Long | 
val categoryId : kotlin.Long = 789 // kotlin.Long | 
val from : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val to : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val search : kotlin.String = search_example // kotlin.String | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseTransactionResponse = webService.listTransactions(active, type, walletId, categoryId, from, to, search, page, size, sort)
}
```

### Parameters
| **active** | **kotlin.Boolean**|  | [optional] |
| **type** | [**TransactionType**](.md)|  | [optional] [enum: SPENT, INCOME] |
| **walletId** | **kotlin.Long**|  | [optional] |
| **categoryId** | **kotlin.Long**|  | [optional] |
| **from** | **java.time.LocalDate**|  | [optional] |
| **to** | **java.time.LocalDate**|  | [optional] |
| **search** | **kotlin.String**|  | [optional] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;createdAt,DESC&quot;)] |

### Return type

[**PageResponseTransactionResponse**](PageResponseTransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Eliminar transaccion

Deshabilita una transaccion.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.transactionDelete(id)
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


Actualizar transaccion

Actualiza una transaccion.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val transactionRequest : TransactionRequest =  // TransactionRequest | 

launch(Dispatchers.IO) {
    val result : TransactionResponse = webService.updateTransaction(id, transactionRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **transactionRequest** | [**TransactionRequest**](TransactionRequest.md)|  | |

### Return type

[**TransactionResponse**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

