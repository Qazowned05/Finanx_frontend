# WebhooksApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**receiveWebhook**](WebhooksApi.md#receiveWebhook) | **POST** webhooks/{provider} | Receive provider webhook |



Receive provider webhook

Receives an inbound webhook notification from a payment provider.

### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(WebhooksApi::class.java)
val provider : PaymentProvider =  // PaymentProvider | Payment provider
val body : kotlin.String = body_example // kotlin.String | 
val xSignature : kotlin.String = xSignature_example // kotlin.String | Signature header sent by the provider

launch(Dispatchers.IO) {
    webService.receiveWebhook(provider, body, xSignature)
}
```

### Parameters
| **provider** | [**PaymentProvider**](.md)| Payment provider | [enum: MERCADO_PAGO, CULQI, STRIPE, MANUAL_TRANSFER] |
| **body** | **kotlin.String**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **xSignature** | **kotlin.String**| Signature header sent by the provider | [optional] |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

