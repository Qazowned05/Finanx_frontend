# AuthControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**google**](AuthControllerApi.md#google) | **POST** auth/google |  |
| [**login**](AuthControllerApi.md#login) | **POST** auth/login |  |
| [**register1**](AuthControllerApi.md#register1) | **POST** auth/register |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AuthControllerApi::class.java)
val googleLoginRequest : GoogleLoginRequest =  // GoogleLoginRequest | 

launch(Dispatchers.IO) {
    val result : LoginResponse = webService.google(googleLoginRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **googleLoginRequest** | [**GoogleLoginRequest**](GoogleLoginRequest.md)|  | |

### Return type

[**LoginResponse**](LoginResponse.md)

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
val webService = apiClient.createWebservice(AuthControllerApi::class.java)
val loginRequest : LoginRequest =  // LoginRequest | 

launch(Dispatchers.IO) {
    val result : LoginResponse = webService.login(loginRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **loginRequest** | [**LoginRequest**](LoginRequest.md)|  | |

### Return type

[**LoginResponse**](LoginResponse.md)

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
val webService = apiClient.createWebservice(AuthControllerApi::class.java)
val userRequest : UserRequest =  // UserRequest | 

launch(Dispatchers.IO) {
    val result : LoginResponse = webService.register1(userRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userRequest** | [**UserRequest**](UserRequest.md)|  | |

### Return type

[**LoginResponse**](LoginResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

