# CategoryControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**activate**](CategoryControllerApi.md#activate) | **POST** categories/{id}/activate |  |
| [**create4**](CategoryControllerApi.md#create4) | **POST** categories |  |
| [**disable4**](CategoryControllerApi.md#disable4) | **POST** categories/{id}/disable |  |
| [**findAllByUser2**](CategoryControllerApi.md#findAllByUser2) | **GET** categories/{idUser} |  |
| [**findAllByUserAndActive**](CategoryControllerApi.md#findAllByUserAndActive) | **GET** categories/{idUser}/{active} |  |
| [**findById5**](CategoryControllerApi.md#findById5) | **GET** categories/{id} |  |
| [**update5**](CategoryControllerApi.md#update5) | **PUT** categories/{id} |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoryControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : CategoryResponse = webService.activate(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**CategoryResponse**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoryControllerApi::class.java)
val categoryRequest : CategoryRequest =  // CategoryRequest | 

launch(Dispatchers.IO) {
    val result : CategoryResponse = webService.create4(categoryRequest)
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




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoryControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.disable4(id)
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




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoryControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CategoryResponse> = webService.findAllByUser2(idUser)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **idUser** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;CategoryResponse&gt;**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoryControllerApi::class.java)
val idUser : kotlin.Long = 789 // kotlin.Long | 
val active : kotlin.Boolean = true // kotlin.Boolean | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CategoryResponse> = webService.findAllByUserAndActive(idUser, active)
}
```

### Parameters
| **idUser** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **active** | **kotlin.Boolean**|  | |

### Return type

[**kotlin.collections.List&lt;CategoryResponse&gt;**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoryControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : CategoryResponse = webService.findById5(id)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long**|  | |

### Return type

[**CategoryResponse**](CategoryResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(CategoryControllerApi::class.java)
val id : kotlin.Long = 789 // kotlin.Long | 
val categoryRequest : CategoryRequest =  // CategoryRequest | 

launch(Dispatchers.IO) {
    val result : CategoryResponse = webService.update5(id, categoryRequest)
}
```

### Parameters
| **id** | **kotlin.Long**|  | |
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

