
# TransactionResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico de la transaccion |  [optional] |
| **walletId** | **kotlin.Long** | Identificador unico de la billetera asociada a la transaccion |  [optional] |
| **walletCurrency** | **kotlin.String** | Moneda de la billetera |  [optional] |
| **walletPrimary** | **kotlin.Boolean** | Indica si la billetera es principal |  [optional] |
| **categoryId** | **kotlin.Long** | Identificador unico de la categoria asociada a la transaccion |  [optional] |
| **categoryName** | **kotlin.String** | Nombre de la categoria asociada a la transaccion |  [optional] |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion, si aplica |  [optional] |
| **reasonDescription** | **kotlin.String** | Descripcion del motivo asociado a la transaccion |  [optional] |
| **savingGoalId** | **kotlin.Long** | Identificador de la meta de ahorro asociada, si aplica |  [optional] |
| **scheduleId** | **kotlin.Long** | Identificador de la programacion asociada, si aplica |  [optional] |
| **receiptId** | **kotlin.Long** | Identificador del recibo asociado, si aplica |  [optional] |
| **receiptImageId** | **kotlin.Long** | Identificador de la imagen del recibo asociado, si aplica |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion |  [optional] |
| **paymentMethod** | [**PaymentMethod**](PaymentMethod.md) |  |  [optional] |
| **type** | [**TransactionType**](TransactionType.md) |  |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion de la transaccion |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la transaccion fue deshabilitada, si aplica |  [optional] |
| **active** | **kotlin.Boolean** | Indica si la transaccion esta activa |  [optional] |



