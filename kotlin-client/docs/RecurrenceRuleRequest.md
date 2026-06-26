
# RecurrenceRuleRequest

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **recurrenceType** | [**inline**](#RecurrenceType) | Tipo de recurrencia (diaria, semanal, mensual, anual) |  |
| **interval** | **kotlin.Int** | Intervalo de repeticion segun el tipo de recurrencia |  |
| **start** | [**java.time.LocalDate**](java.time.LocalDate.md) | Fecha de inicio de la recurrencia (formato yyyy-MM-dd) |  |
| **end** | [**java.time.LocalDate**](java.time.LocalDate.md) | Fecha de fin de la recurrencia (formato yyyy-MM-dd) |  |
| **dayOfWeek** | [**inline**](#DayOfWeek) | Dia especifico de la semana en que aplica la recurrencia (solo relevante si recurrenceType &#x3D; WEEKLY) |  [optional] |


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



