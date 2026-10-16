
# ReceiptResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **receiptId** | **kotlin.Long** | Identificador del recibo escaneado |  [optional] |
| **status** | [**ReceiptScanStatus**](ReceiptScanStatus.md) |  |  [optional] |
| **errorMessage** | **kotlin.String** | Mensaje de error si el escaneo falló (nullable) |  [optional] |
| **imageId** | **kotlin.Long** | ID de la imagen asociada (nullable si no tiene imagen) |  [optional] |
| **detectedAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto total detectado |  [optional] |
| **detectedDate** | [**java.time.LocalDate**](java.time.LocalDate.md) | Fecha detectada en el recibo (nullable) |  [optional] |
| **description** | **kotlin.String** | Descripción/nombre del comercio detectado |  [optional] |
| **paymentMethod** | [**PaymentMethod**](PaymentMethod.md) |  |  [optional] |
| **transactionType** | [**TransactionType**](TransactionType.md) |  |  [optional] |
| **detectedCategoryId** | **kotlin.Long** | ID de la categoría detectada (nullable) |  [optional] |
| **rawOcrText** | **kotlin.String** | Texto OCR crudo extraído de la imagen |  [optional] |
| **currencyGuess** | **kotlin.String** | Moneda detectada (no resuelta contra Wallet) |  [optional] |
| **confidenceScore** | **kotlin.Double** | Nivel de confianza del parseo |  [optional] |
| **parserEngineUsed** | **kotlin.String** | Motor usado para el parseo (REGEX o GEMINI) |  [optional] |



