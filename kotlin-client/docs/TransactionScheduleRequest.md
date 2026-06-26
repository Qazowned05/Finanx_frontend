
# TransactionScheduleRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion programada (debe ser mayor a 0) |  |
| **paymentMethod** | [**inline**](#PaymentMethod) | Metodo de pago de la transaccion |  |
| **transactionType** | [**inline**](#TransactionType) | Tipo de transaccion (ingreso o gasto) |  |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion |  [optional] |
| **description** | **kotlin.String** | Descripcion de la transaccion programada |  [optional] |


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



