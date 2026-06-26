# TransactionsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createTransaction**](TransactionsApi.md#createTransaction) | **POST** transactions | Crear transaccion |
| [**findAllActiveTransactionByUser**](TransactionsApi.md#findAllActiveTransactionByUser) | **GET** transactions/user/{userId}/active | Listar transacciones activas |
| [**findAllTransactionByCategoryAndDateBetween**](TransactionsApi.md#findAllTransactionByCategoryAndDateBetween) | **GET** transactions/user/{userId}/category/{categoryId}/between | Filtrar por categoria y fecha |
| [**findAllTransactionByUser**](TransactionsApi.md#findAllTransactionByUser) | **GET** transactions/user/{userId} | Listar transacciones por usuario |
| [**findAllTransactionsByUserBetween**](TransactionsApi.md#findAllTransactionsByUserBetween) | **GET** transactions/user/{userId}/between | Listar transacciones por rango |
| [**findTransactionById**](TransactionsApi.md#findTransactionById) | **GET** transactions/{id} | Buscar transaccion |
| [**transactionDelete**](TransactionsApi.md#transactionDelete) | **DELETE** transactions/{id} | Eliminar transaccion |
| [**updateTransaction**](TransactionsApi.md#updateTransaction) | **PUT** transactions/{id} | Actualizar transaccion |



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


Listar transacciones activas

Obtiene todas las transacciones activas de un usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | ID usuario

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionResponse> = webService.findAllActiveTransactionByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**| ID usuario | |

### Return type

[**kotlin.collections.List&lt;TransactionResponse&gt;**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Filtrar por categoria y fecha

Obtiene transacciones por categoria y rango de fechas.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val categoryId : kotlin.Long = 789 // kotlin.Long | 
val from : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val to : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionResponse> = webService.findAllTransactionByCategoryAndDateBetween(userId, categoryId, from, to)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **categoryId** | **kotlin.Long**|  | |
| **from** | **java.time.LocalDate**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **to** | **java.time.LocalDate**|  | |

### Return type

[**kotlin.collections.List&lt;TransactionResponse&gt;**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar transacciones por usuario

Obtiene todas las transacciones de un usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionResponse> = webService.findAllTransactionByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;TransactionResponse&gt;**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar transacciones por rango

Obtiene transacciones en un rango de fechas.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(TransactionsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val from : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 
val to : java.time.LocalDate = 2013-10-20 // java.time.LocalDate | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TransactionResponse> = webService.findAllTransactionsByUserBetween(userId, from, to)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **from** | **java.time.LocalDate**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **to** | **java.time.LocalDate**|  | |

### Return type

[**kotlin.collections.List&lt;TransactionResponse&gt;**](TransactionResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
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

