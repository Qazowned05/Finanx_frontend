# FCMTokensApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**listFcmTokens**](FCMTokensApi.md#listFcmTokens) | **GET** users/{userId}/fcm-tokens | Listar tokens FCM del usuario |
| [**registerFcmToken**](FCMTokensApi.md#registerFcmToken) | **POST** users/{userId}/fcm-tokens | Registrar token FCM |
| [**removeFcmToken**](FCMTokensApi.md#removeFcmToken) | **DELETE** users/{userId}/fcm-tokens/{tokenId} | Eliminar token FCM |



Listar tokens FCM del usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(FCMTokensApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<FcmTokenResponse> = webService.listFcmTokens(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

[**kotlin.collections.List&lt;FcmTokenResponse&gt;**](FcmTokenResponse.md)

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
val userId : kotlin.Long = 789 // kotlin.Long | 
val registerFcmTokenRequest : RegisterFcmTokenRequest =  // RegisterFcmTokenRequest | 

launch(Dispatchers.IO) {
    val result : FcmTokenResponse = webService.registerFcmToken(userId, registerFcmTokenRequest)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
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
val userId : kotlin.Long = 789 // kotlin.Long | 
val tokenId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    webService.removeFcmToken(userId, tokenId)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
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

