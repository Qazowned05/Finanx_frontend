# UserControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**findAllUser**](UserControllerApi.md#findAllUser) | **GET** users | Listar todos los usuarios |



Listar todos los usuarios

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserControllerApi::class.java)

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<UserResponse> = webService.findAllUser()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**kotlin.collections.List&lt;UserResponse&gt;**](UserResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

