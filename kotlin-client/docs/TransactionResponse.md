
# TransactionResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico de la transaccion |  [optional] |
| **userId** | **kotlin.Long** | Identificador unico del usuario asociado a la transaccion |  [optional] |
| **categoryId** | **kotlin.Long** | Identificador unico de la categoria asociada a la transaccion |  [optional] |
| **categoryName** | **kotlin.String** | Nombre de la categoria asociada a la transaccion |  [optional] |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion, si aplica |  [optional] |
| **reasonDescription** | **kotlin.String** | Descripcion del motivo asociado a la transaccion |  [optional] |
| **savingGoalId** | **kotlin.Long** | Identificador de la meta de ahorro asociada, si aplica |  [optional] |
| **scheduleId** | **kotlin.Long** | Identificador de la programacion asociada, si aplica |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion |  [optional] |
| **paymentMethod** | [**inline**](#PaymentMethod) | Metodo de pago utilizado en la transaccion |  [optional] |
| **type** | [**inline**](#Type) | Tipo de transaccion (ingreso o gasto) |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion de la transaccion |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la transaccion fue deshabilitada, si aplica |  [optional] |
| **active** | **kotlin.Boolean** | Indica si la transaccion esta activa |  [optional] |


<a id="PaymentMethod"></a>
## Enum: paymentMethod
| Name | Value |
| ---- | ----- |
| paymentMethod | CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, YAPE, PLIN, OTHER |


<a id="Type"></a>
## Enum: type
| Name | Value |
| ---- | ----- |
| type | SPENT, INCOME |



