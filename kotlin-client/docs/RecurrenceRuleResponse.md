
# RecurrenceRuleResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico de la regla de recurrencia |  [optional] |
| **recurrenceType** | [**RecurrenceType**](RecurrenceType.md) |  |  [optional] |
| **interval** | **kotlin.Int** | Intervalo de repeticion segun el tipo de recurrencia |  [optional] |
| **dayOfWeek** | [**DayOfWeek**](DayOfWeek.md) |  |  [optional] |
| **start** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de inicio de la recurrencia |  [optional] |
| **end** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de fin de la recurrencia |  [optional] |
| **durationDays** | **kotlin.Long** | Duracion total del periodo de recurrencia en dias |  [optional] |



