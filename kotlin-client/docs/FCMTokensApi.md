# FCMTokensApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**listFcmTokens**](FCMTokensApi.md#listFcmTokens) | **GET** api/v1/fcm-tokens | Listar tokens FCM del usuario |
| [**registerFcmToken**](FCMTokensApi.md#registerFcmToken) | **POST** api/v1/fcm-tokens | Registrar token FCM |
| [**removeFcmToken**](FCMTokensApi.md#removeFcmToken) | **DELETE** api/v1/fcm-tokens/{tokenId} | Eliminar token FCM |



Listar tokens FCM del usuario

Lista paginada con page, size y sort estándar.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FCMTokensApi::class.java)
val page : kotlin.Int = 56 // kotlin.Int | Zero-based page index (0..N)
val size : kotlin.Int = 56 // kotlin.Int | The size of the page to be returned
val sort : kotlin.collections.List<kotlin.String> =  // kotlin.collections.List<kotlin.String> | Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported.

launch(Dispatchers.IO) {
    val result : PageResponseFcmTokenResponse = webService.listFcmTokens(page, size, sort)
}
```

### Parameters
| **page** | **kotlin.Int**| Zero-based page index (0..N) | [optional] [default to 0] |
| **size** | **kotlin.Int**| The size of the page to be returned | [optional] [default to 50] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **sort** | [**kotlin.collections.List&lt;kotlin.String&gt;**](kotlin.String.md)| Sorting criteria in the format: property,(asc|desc). Default sort order is ascending. Multiple sort criteria are supported. | [optional] [default to arrayListOf(&quot;createdAt,DESC&quot;)] |

### Return type

[**PageResponseFcmTokenResponse**](PageResponseFcmTokenResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Registrar token FCM

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FCMTokensApi::class.java)
val registerFcmTokenRequest : RegisterFcmTokenRequest =  // RegisterFcmTokenRequest | 

launch(Dispatchers.IO) {
    val result : FcmTokenResponse = webService.registerFcmToken(registerFcmTokenRequest)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **registerFcmTokenRequest** | [**RegisterFcmTokenRequest**](RegisterFcmTokenRequest.md)|  | |

### Return type

[**FcmTokenResponse**](FcmTokenResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Eliminar token FCM

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FCMTokensApi::class.java)
val tokenId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.removeFcmToken(tokenId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **tokenId** | **kotlin.Long**|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

