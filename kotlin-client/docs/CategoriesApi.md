# CategoriesApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createCategory**](CategoriesApi.md#createCategory) | **POST** categories | Crear categoria |
| [**disableCategory**](CategoriesApi.md#disableCategory) | **POST** categories/{id}/disable | Deshabilitar categoria |
| [**findAllCategoriesByUser**](CategoriesApi.md#findAllCategoriesByUser) | **GET** categories/user/{idUser} | Listar categorias por usuario |
| [**findAllCategoriesyUserAndActive**](CategoriesApi.md#findAllCategoriesyUserAndActive) | **GET** categories/user/{idUser}/{active} | Listar categorias por usuario y estado |
| [**findCategoryById**](CategoriesApi.md#findCategoryById) | **GET** categories/{id} | Buscar categoria por ID |
| [**updateCategory**](CategoriesApi.md#updateCategory) | **PUT** categories/{id} | Actualizar categoria |



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


Listar categorias por usuario

Obtiene todas las categorias asociadas a un usuario.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoriesApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID del usuario

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CategoryResponse> = webService.findAllCategoriesByUser(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**| ID del usuario | |

### Return type

[**kotlin.collections.List&lt;CategoryResponse&gt;**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Listar categorias por usuario y estado

Obtiene categorias filtradas por estado activo/inactivo.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoriesApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | ID del usuario
val active : kotlin.Boolean = true // kotlin.Boolean | Estado activo

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CategoryResponse> = webService.findAllCategoriesyUserAndActive(idUser, active)
}
```

### Parameters
| **idUser** | **kotlin.Long**| ID del usuario | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **active** | **kotlin.Boolean**| Estado activo | |

### Return type

[**kotlin.collections.List&lt;CategoryResponse&gt;**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


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

