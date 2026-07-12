# ImagesApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**downloadImage**](ImagesApi.md#downloadImage) | **GET** images/{imageId} | Descargar imagen |



Descargar imagen

Retorna los bytes de una imagen por su ID.

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
    val result : kotlin.ByteArray = webService.downloadImage(imageId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **imageId** | **kotlin.Long**| ID de la imagen | |

### Return type

**kotlin.ByteArray**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

