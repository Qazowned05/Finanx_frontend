
# CreateScheduleRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** | Identificador unico del usuario asociado a la programacion |  |
| **categoryId** | **kotlin.Long** | Identificador unico de la categoria asociada a la programacion |  |
| **type** | **kotlin.String** |  |  |
| **alertable** | **kotlin.Boolean** | Indica si el presupuesto debe generar alertas |  |
| **percentAlert** | **kotlin.Int** | Porcentaje de alerta respecto al limite del presupuesto |  |
| **limitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto limite del presupuesto |  |
| **description** | **kotlin.String** | Descripcion de la transaccion programada |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion programada (debe ser mayor a 0) |  |
| **paymentMethod** | [**inline**](#PaymentMethod) | Metodo de pago de la transaccion |  |
| **transactionType** | [**inline**](#TransactionType) | Tipo de transaccion (ingreso o gasto) |  |
| **recurrenceRule** | [**RecurrenceRuleRequest**](RecurrenceRuleRequest.md) | Regla de recurrencia que define la periodicidad de la programacion |  [optional] |
| **walletId** | **kotlin.Long** | Identificador de la billetera (null &#x3D; usar la principal) |  [optional] |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion |  [optional] |


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



