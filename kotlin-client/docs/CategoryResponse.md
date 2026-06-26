
# CategoryResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico de la categoria |  [optional] |
| **name** | **kotlin.String** | Nombre de la categoria |  [optional] |
| **description** | **kotlin.String** | Descripcion detallada de la categoria |  [optional] |
| **type** | [**inline**](#Type) | Tipo de transaccion asociado a la categoria |  [optional] |
| **active** | **kotlin.Boolean** | Indica si la categoria esta activa |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion de la categoria |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la categoria fue deshabilitada, si aplica |  [optional] |


<a id="Type"></a>
## Enum: type
| Name | Value |
| ---- | ----- |
| type | SPENT, INCOME |



