
# ScheduleResponse

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
| **state** | [**ScheduleState**](ScheduleState.md) |  |  [optional] |
| **recurrenceRule** | [**RecurrenceRuleResponse**](RecurrenceRuleResponse.md) |  |  [optional] |



