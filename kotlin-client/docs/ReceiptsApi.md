# ReceiptsApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getScanResult**](ReceiptsApi.md#getScanResult) | **GET** api/v1/receipts/{receiptId} | Obtener resultado de escaneo |
| [**improveScan**](ReceiptsApi.md#improveScan) | **POST** api/v1/receipts/{receiptId}/improve | Mejorar parseo con IA |
| [**scanReceipt**](ReceiptsApi.md#scanReceipt) | **POST** api/v1/receipts/scan | Encolar escaneo OCR de recibo |



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

launch(Dispatchers.IO) {
    val result : ReceiptResponse = webService.getScanResult(receiptId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **receiptId** | **kotlin.Long**|  | |

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

launch(Dispatchers.IO) {
    val result : ReceiptResponse = webService.improveScan(receiptId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **receiptId** | **kotlin.Long**| ID del recibo | |

### Return type

[**ReceiptResponse**](ReceiptResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*


Encolar escaneo OCR de recibo

Sube la imagen, crea el recibo en estado PENDING y encola el OCR en el microservicio doc-processing. Responde 202 con la Location para consultar el resultado. Requiere plan PREMIUM.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(ReceiptsApi::class.java)
val image : java.io.File = BINARY_DATA_HERE // java.io.File | Imagen del recibo (max 5MB)

launch(Dispatchers.IO) {
    val result : ReceiptResponse = webService.scanReceipt(image)
}
```

### Parameters
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

