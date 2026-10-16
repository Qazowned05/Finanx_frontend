
# UpdateSchedule400Response

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **type** | **kotlin.String** |  |  |
| **id** | **kotlin.Long** | Identificador unico de la programacion |  [optional] |
| **userId** | **kotlin.Long** | Identificador unico del usuario asociado a la programacion |  [optional] |
| **categoryId** | **kotlin.Long** | Identificador unico de la categoria asociada a la programacion |  [optional] |
| **categoryName** | **kotlin.String** | Nombre de la categoria asociada a la programacion |  [optional] |
| **nextRunAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de la proxima ejecucion programada |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion de la programacion |  [optional] |
| **lastRunAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de la ultima ejecucion realizada |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la programacion fue deshabilitada, si aplica |  [optional] |
| **active** | **kotlin.Boolean** | Indica si la programacion esta activa |  [optional] |
| **state** | [**inline**](#State) | Estado actual de la programacion |  [optional] |
| **recurrenceRule** | [**RecurrenceRuleResponse**](RecurrenceRuleResponse.md) | Regla de recurrencia asociada a la programacion |  [optional] |
| **alertable** | **kotlin.Boolean** | Indica si el presupuesto debe generar alertas |  [optional] |
| **percentAlert** | **kotlin.Int** | Porcentaje de alerta respecto al limite del presupuesto |  [optional] |
| **limitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto limite del presupuesto |  [optional] |
| **description** | **kotlin.String** | Descripcion del presupuesto |  [optional] |
| **walletId** | **kotlin.Long** | Identificador de la billetera |  [optional] |
| **walletCurrency** | **kotlin.String** | Moneda de la billetera |  [optional] |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion |  [optional] |
| **reasonDesc** | **kotlin.String** | Descripcion del motivo asociado a la transaccion |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion programada |  [optional] |
| **paymentMethod** | [**inline**](#PaymentMethod) | Metodo de pago de la transaccion |  [optional] |
| **transactionType** | [**inline**](#TransactionType) | Tipo de transaccion (ingreso o gasto) |  [optional] |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | RUNNING, PAUSED, FINALIZED, CANCELLED, CREATED, ERROR |


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



