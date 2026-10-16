# UserPreferencesApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**createUserPreferences**](UserPreferencesApi.md#createUserPreferences) | **POST** api/v1/users/me/preferences | Crear preferencias del usuario |
| [**getUserPreferences**](UserPreferencesApi.md#getUserPreferences) | **GET** api/v1/users/me/preferences | Obtener preferencias del usuario |
| [**updateUserPreferences**](UserPreferencesApi.md#updateUserPreferences) | **PUT** api/v1/users/me/preferences | Actualizar preferencias del usuario |



Crear preferencias del usuario

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(UserPreferencesApi::class.java)
val userPreferencesRequest : UserPreferencesRequest =  // UserPreferencesRequest | 

launch(Dispatchers.IO) {
    val result : UserPreferencesResponse = webService.createUserPreferences(userPreferencesRequest)
}
```

### Parameters
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

launch(Dispatchers.IO) {
    val result : UserPreferencesResponse = webService.getUserPreferences()
}
```

### Parameters
This endpoint does not need any parameter.

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
val userPreferencesRequest : UserPreferencesRequest =  // UserPreferencesRequest | 

launch(Dispatchers.IO) {
    val result : UserPreferencesResponse = webService.updateUserPreferences(userPreferencesRequest)
}
```

### Parameters
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

