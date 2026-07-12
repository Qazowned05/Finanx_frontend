
# ReceiptResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **receiptId** | **kotlin.Long** | Identificador del recibo escaneado |  [optional] |
| **imageId** | **kotlin.Long** | ID de la imagen asociada (nullable si no tiene imagen) |  [optional] |
| **detectedAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto total detectado |  [optional] |
| **detectedDate** | [**java.time.LocalDate**](java.time.LocalDate.md) | Fecha detectada en el recibo (nullable) |  [optional] |
| **description** | **kotlin.String** | DescripciÃ³n/nombre del comercio detectado |  [optional] |
| **paymentMethod** | [**inline**](#PaymentMethod) | MÃ©todo de pago detectado |  [optional] |
| **transactionType** | [**inline**](#TransactionType) | Tipo de transacciÃ³n detectado |  [optional] |
| **detectedCategoryId** | **kotlin.Long** | ID de la categorÃ­a detectada (nullable) |  [optional] |
| **rawOcrText** | **kotlin.String** | Texto OCR crudo extraÃ­do de la imagen |  [optional] |
| **currencyGuess** | **kotlin.String** | Moneda detectada (no resuelta contra Wallet) |  [optional] |
| **confidenceScore** | **kotlin.Double** | Nivel de confianza del parseo |  [optional] |
| **parserEngineUsed** | **kotlin.String** | Motor usado para el parseo (REGEX o GEMINI) |  [optional] |


<a id="PaymentMethod"></a>
## Enum: paymentMethod
| Name | Value |
| ---- | ----- |
| paymentMethod | CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, YAPE, PLIN, OTHER |


<a id="TransactionType"></a>
## Enum: transactionType
| Name | Value |
| ---- | ----- |
| transactionType | SPENT, INCOME |



