
# TransactionScheduleRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **amount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto de la transaccion programada (debe ser mayor a 0) |  |
| **paymentMethod** | [**PaymentMethod**](PaymentMethod.md) |  |  |
| **transactionType** | [**TransactionType**](TransactionType.md) |  |  |
| **walletId** | **kotlin.Long** | Identificador de la billetera (null &#x3D; usar la principal) |  [optional] |
| **reasonId** | **kotlin.Long** | Identificador del motivo asociado a la transaccion |  [optional] |
| **description** | **kotlin.String** | Descripcion de la transaccion programada |  [optional] |



