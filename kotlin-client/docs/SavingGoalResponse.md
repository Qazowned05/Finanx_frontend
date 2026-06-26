
# SavingGoalResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico de la meta de ahorro |  [optional] |
| **description** | **kotlin.String** | Descripcion de la meta de ahorro |  [optional] |
| **targetAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto objetivo de la meta de ahorro |  [optional] |
| **accumulated** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto acumulado hasta el momento |  [optional] |
| **deadline** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha limite para alcanzar la meta |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion de la meta |  [optional] |
| **completedAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la meta fue completada, si aplica |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la meta fue deshabilitada, si aplica |  [optional] |
| **state** | [**inline**](#State) | Estado actual de la meta de ahorro |  [optional] |
| **active** | **kotlin.Boolean** | Indica si la meta esta activa |  [optional] |
| **userId** | **kotlin.Long** | Identificador unico del usuario asociado a la meta |  [optional] |
| **progressPercentage** | **kotlin.Double** | Porcentaje de progreso hacia la meta |  [optional] |
| **transactionsCount** | **kotlin.Int** | Cantidad de transacciones asociadas a la meta |  [optional] |
| **outstanding** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto pendiente para alcanzar la meta |  [optional] |
| **daysRemaining** | **kotlin.Long** | Numero de dias restantes hasta la fecha limite |  [optional] |
| **averageContribution** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto promedio aportado por transaccion |  [optional] |
| **completed** | **kotlin.Boolean** | Indica si la meta ya fue completada |  [optional] |
| **estimatedCompletionDate** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha estimada de cumplimiento de la meta, calculada en base al ritmo de aportes |  [optional] |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | RUNNING, COMPLETED, CANCELLED |



