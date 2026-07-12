# ReceiptsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getScanResult**](ReceiptsApi.md#getScanResult) | **GET** receipts/{receiptId} | Obtener resultado de escaneo |
| [**improveScan**](ReceiptsApi.md#improveScan) | **POST** receipts/{receiptId}/improve | Mejorar parseo con IA |
| [**scanReceipt**](ReceiptsApi.md#scanReceipt) | **POST** receipts/scan | Escanear recibo con OCR |



Obtener resultado de escaneo

Recupera el resultado de un escaneo previo por ID.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReceiptsApi::class.java)
val receiptId : kotlin.Long = 789 // kotlin.Long | 
val userId : kotlin.Long = 789 // kotlin.Long | ID del usuario

launch(Dispatchers.IO) {
    val result : ReceiptResponse = webService.getScanResult(receiptId, userId)
}
```

### Parameters
| **receiptId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**| ID del usuario | |

### Return type

[**ReceiptResponse**](ReceiptResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Mejorar parseo con IA

Re-ejecuta GeminiParser sobre el texto OCR guardado. Actualiza el recibo solo si la confianza mejora.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReceiptsApi::class.java)
val receiptId : kotlin.Long = 789 // kotlin.Long | ID del recibo
val userId : kotlin.Long = 789 // kotlin.Long | ID del usuario

launch(Dispatchers.IO) {
    val result : ReceiptResponse = webService.improveScan(receiptId, userId)
}
```

### Parameters
| **receiptId** | **kotlin.Long**| ID del recibo | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**| ID del usuario | |

### Return type

[**ReceiptResponse**](ReceiptResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Escanear recibo con OCR

Procesa una imagen de recibo, extrae texto via Tesseract y parsea los datos (monto, fecha, comercio). Requiere plan PREMIUM.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReceiptsApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | ID del usuario
val image : java.io.File = BINARY_DATA_HERE // java.io.File | Imagen del recibo (max 5MB)

launch(Dispatchers.IO) {
    val result : ReceiptResponse = webService.scanReceipt(userId, image)
}
```

### Parameters
| **userId** | **kotlin.Long**| ID del usuario | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **image** | **java.io.File**| Imagen del recibo (max 5MB) | |

### Return type

[**ReceiptResponse**](ReceiptResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: */*

