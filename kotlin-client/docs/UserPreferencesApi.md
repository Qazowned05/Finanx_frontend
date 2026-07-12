# UserPreferencesApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createUserPreferences**](UserPreferencesApi.md#createUserPreferences) | **POST** users/{userId}/preferences | Crear preferencias del usuario |
| [**getUserPreferences**](UserPreferencesApi.md#getUserPreferences) | **GET** users/{userId}/preferences | Obtener preferencias del usuario |
| [**updateUserPreferences**](UserPreferencesApi.md#updateUserPreferences) | **PUT** users/{userId}/preferences | Actualizar preferencias del usuario |



Crear preferencias del usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserPreferencesApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val userPreferencesRequest : UserPreferencesRequest =  // UserPreferencesRequest | 

launch(Dispatchers.IO) {
    val result : UserPreferencesResponse = webService.createUserPreferences(userId, userPreferencesRequest)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userPreferencesRequest** | [**UserPreferencesRequest**](UserPreferencesRequest.md)|  | |

### Return type

[**UserPreferencesResponse**](UserPreferencesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*


Obtener preferencias del usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserPreferencesApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : UserPreferencesResponse = webService.getUserPreferences(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

[**UserPreferencesResponse**](UserPreferencesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Actualizar preferencias del usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserPreferencesApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val userPreferencesRequest : UserPreferencesRequest =  // UserPreferencesRequest | 

launch(Dispatchers.IO) {
    val result : UserPreferencesResponse = webService.updateUserPreferences(userId, userPreferencesRequest)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userPreferencesRequest** | [**UserPreferencesRequest**](UserPreferencesRequest.md)|  | |

### Return type

[**UserPreferencesResponse**](UserPreferencesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

