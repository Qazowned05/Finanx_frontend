
# RecurrenceRuleResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico de la regla de recurrencia |  [optional] |
| **recurrenceType** | [**inline**](#RecurrenceType) | Tipo de recurrencia (diaria, semanal, mensual, anual) |  [optional] |
| **interval** | **kotlin.Int** | Intervalo de repeticion segun el tipo de recurrencia |  [optional] |
| **dayOfWeek** | [**inline**](#DayOfWeek) | Dia especifico de la semana en que aplica la recurrencia (solo relevante si recurrenceType &#x3D; WEEKLY) |  [optional] |
| **start** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de inicio de la recurrencia |  [optional] |
| **end** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de fin de la recurrencia |  [optional] |
| **durationDays** | **kotlin.Long** | Duracion total del periodo de recurrencia en dias |  [optional] |


<a id="RecurrenceType"></a>
## Enum: recurrenceType
| Name | Value |
| ---- | ----- |
| recurrenceType | NONE, DAILY, WEEKLY, MONTHLY, YEARLY |


<a id="DayOfWeek"></a>
## Enum: dayOfWeek
| Name | Value |
| ---- | ----- |
| dayOfWeek | MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY |



