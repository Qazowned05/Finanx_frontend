# ImagesApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**downloadImage**](ImagesApi.md#downloadImage) | **GET** api/v1/images/{imageId} | Descargar imagen |



Descargar imagen

Redirige a una URL prefirmada de corta duración después de validar que la imagen pertenece al usuario autenticado.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ImagesApi::class.java)
val imageId : kotlin.Long = 789 // kotlin.Long | ID de la imagen

launch(Dispatchers.IO) {
    webService.downloadImage(imageId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **imageId** | **kotlin.Long**| ID de la imagen | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

