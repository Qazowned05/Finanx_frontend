
# TransactionRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **walletId** | **kotlin.Long** | Identificador unico de la billetera asociada a la transaccion |  |
| **categoryId** | **kotlin.Long** | Identificador unico de la categoria asociada a la transaccion |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion (debe ser mayor a 0) |  |
| **paymentMethod** | [**PaymentMethod**](PaymentMethod.md) |  |  |
| **type** | [**TransactionType**](TransactionType.md) |  |  |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion, si aplica |  [optional] |
| **description** | **kotlin.String** | Descripcion de la transaccion |  [optional] |
| **savingGoalId** | **kotlin.Long** | Identificador de la meta de ahorro asociada, si aplica |  [optional] |
| **receiptId** | **kotlin.Long** | Identificador del recibo escaneado vía OCR, si aplica |  [optional] |
| **convertCurrency** | **kotlin.Boolean** | Si true y receiptId presente, convierte el monto del recibo (currencyGuess) a la moneda del wallet usando el exchange service |  [optional] |



