
# TransactionScheduleResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion |  [optional] |
| **reasonDesc** | **kotlin.String** | Descripcion del motivo asociado a la transaccion |  [optional] |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion programada |  [optional] |
| **paymentMethod** | [**inline**](#PaymentMethod) | Metodo de pago de la transaccion |  [optional] |
| **transactionType** | [**inline**](#TransactionType) | Tipo de transaccion (ingreso o gasto) |  [optional] |


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



