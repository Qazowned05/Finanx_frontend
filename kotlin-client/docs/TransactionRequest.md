
# TransactionRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long** | Identificador unico del usuario asociado a la transaccion |  |
| **categoryId** | **kotlin.Long** | Identificador unico de la categoria asociada a la transaccion |  |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion (debe ser mayor a 0) |  |
| **paymentMethod** | [**inline**](#PaymentMethod) | Metodo de pago utilizado en la transaccion |  |
| **type** | [**inline**](#Type) | Tipo de transaccion (ingreso o gasto) |  |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion, si aplica |  [optional] |
| **description** | **kotlin.String** | Descripcion de la transaccion |  [optional] |
| **savingGoalId** | **kotlin.Long** | Identificador de la meta de ahorro asociada, si aplica |  [optional] |


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



