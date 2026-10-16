# CategoriesApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createCategory**](CategoriesApi.md#createCategory) | **POST** api/v1/categories | Crear categoria |
| [**disableCategory**](CategoriesApi.md#disableCategory) | **POST** api/v1/categories/{id}/disable | Deshabilitar categoria |
| [**findCategoryById**](CategoriesApi.md#findCategoryById) | **GET** api/v1/categories/{id} | Buscar categoria por ID |
| [**listCategories**](CategoriesApi.md#listCategories) | **GET** api/v1/categories | Listar categorias del usuario |
| [**updateCategory**](CategoriesApi.md#updateCategory) | **PUT** api/v1/categories/{id} | Actualizar categoria |



Crear categoria

Crea una nueva categoria.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoriesApi::class.java)
val categoryRequest : CategoryRequest =  // CategoryRequest | 

launch(Dispatchers.IO) {
    val result : CategoryResponse = webService.createCategory(categoryRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **categoryRequest** | [**CategoryRequest**](CategoryRequest.md)|  | |

### Return type

[**CategoryResponse**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Deshabilitar categoria

Deshabilita una categoria.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoriesApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la categoria

launch(Dispatchers.IO) {
    webService.disableCategory(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la categoria | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


Buscar categoria por ID

Obtiene una categoria especifica.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoriesApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la categoria

launch(Dispatchers.IO) {
    val result : CategoryResponse = webService.findCategoryById(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**| ID de la categoria | |

### Return type

[**CategoryResponse**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar categorias del usuario

Lista paginada del usuario autenticado con filtros opcionales active, type y search.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoriesApi::class.java)
val active : kotlin.Boolean = true // kotlin.Boolean | 
val type : TransactionType =  // TransactionType | 
val search : kotlin.String = search_example // kotlin.String | 
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseCategoryResponse = webService.listCategories(active, type, search, page, size, sort)
}
```

### Parameters
| **active** | **kotlin.Boolean**|  | [optional] |
| **type** | [**TransactionType**](.md)|  | [optional] [enum: SPENT, INCOME] |
| **search** | **kotlin.String**|  | [optional] |
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;name,ASC&quot;)] |

### Return type

[**PageResponseCategoryResponse**](PageResponseCategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Actualizar categoria

Actualiza una categoria existente.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoriesApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | ID de la categoria
val categoryRequest : CategoryRequest =  // CategoryRequest | 

launch(Dispatchers.IO) {
    val result : CategoryResponse = webService.updateCategory(id, categoryRequest)
}
```

### Parameters
| **id** | **kotlin.Long**| ID de la categoria | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **categoryRequest** | [**CategoryRequest**](CategoryRequest.md)|  | |

### Return type

[**CategoryResponse**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

