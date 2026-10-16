
# BudgetResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico del presupuesto |  [optional] |
| **userId** | **kotlin.Long** | Identificador del usuario propietario del presupuesto |  [optional] |
| **categoryId** | **kotlin.Long** | Identificador de la categoria asociada al presupuesto |  [optional] |
| **categoryName** | **kotlin.String** | Nombre de la categoria asociada al presupuesto |  [optional] |
| **scheduleId** | **kotlin.Long** | Identificador de la programacion asociada al presupuesto |  [optional] |
| **alertable** | **kotlin.Boolean** | Indica si el presupuesto debe generar alertas |  [optional] |
| **percentAlert** | **kotlin.Int** | Porcentaje de alerta respecto al limite definido |  [optional] |
| **limitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto limite del presupuesto |  [optional] |
| **currencyCode** | **kotlin.String** | Codigo de moneda del presupuesto |  [optional] |
| **description** | **kotlin.String** | Descripcion del presupuesto |  [optional] |
| **state** | [**BudgetState**](BudgetState.md) |  |  [optional] |
| **health** | [**BudgetHealth**](BudgetHealth.md) |  |  [optional] |
| **start** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de inicio del presupuesto |  [optional] |
| **end** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de fin del presupuesto |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion del presupuesto |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de desactivacion del presupuesto |  [optional] |



