# WalletsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**create**](WalletsApi.md#create) | **POST** users/{userId}/wallets | Crear billetera |
| [**disable**](WalletsApi.md#disable) | **DELETE** users/{userId}/wallets/{id} | Deshabilitar billetera |
| [**findAllByUser**](WalletsApi.md#findAllByUser) | **GET** users/{userId}/wallets | Listar billeteras de un usuario |
| [**findById**](WalletsApi.md#findById) | **GET** users/{userId}/wallets/{id} | Obtener billetera por ID |
| [**update**](WalletsApi.md#update) | **PUT** users/{userId}/wallets/{id} | Actualizar billetera |



Crear billetera

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(WalletsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val walletRequest : WalletRequest =  // WalletRequest | 

launch(Dispatchers.IO) {
    val result : WalletResponse = webService.create(userId, walletRequest)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **walletRequest** | [**WalletRequest**](WalletRequest.md)|  | |

### Return type

[**WalletResponse**](WalletResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Deshabilitar billetera

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(WalletsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.disable(userId, id)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
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


Listar billeteras de un usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(WalletsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<WalletResponse> = webService.findAllByUser(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;WalletResponse&gt;**](WalletResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Obtener billetera por ID

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(WalletsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : WalletResponse = webService.findById(userId, id)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**WalletResponse**](WalletResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Actualizar billetera

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(WalletsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val id : kotlin.Long = 789 // kotlin.Long | 
val walletRequest : WalletRequest =  // WalletRequest | 

launch(Dispatchers.IO) {
    val result : WalletResponse = webService.update(userId, id, walletRequest)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **id** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **walletRequest** | [**WalletRequest**](WalletRequest.md)|  | |

### Return type

[**WalletResponse**](WalletResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

