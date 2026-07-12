
# NotificationDTO

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico de la notificacion |  [optional] |
| **userId** | **kotlin.Long** | Identificador del usuario asociado a la notificacion |  [optional] |
| **userEmail** | **kotlin.String** | Correo electronico del usuario |  [optional] |
| **emailNotifications** | **kotlin.Boolean** | Indica si el usuario tiene habilitadas las notificaciones por email |  [optional] |
| **fcmTokens** | **kotlin.collections.List&lt;kotlin.String&gt;** | Tokens FCM del usuario para notificaciones push |  [optional] |
| **type** | [**inline**](#Type) | Tipo de notificacion |  [optional] |
| **message** | **kotlin.String** | Mensaje de la notificacion |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion de la notificacion |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la notificacion fue deshabilitada, si aplica |  [optional] |
| **scheduledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora programada para enviar la notificacion |  [optional] |
| **sentAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que la notificacion fue enviada |  [optional] |
| **state** | [**inline**](#State) | Estado actual de la notificacion |  [optional] |
| **scheduleId** | **kotlin.Long** | Identificador de la programacion asociada |  [optional] |
| **budgetId** | **kotlin.Long** | Identificador del presupuesto asociado |  [optional] |
| **budgetDescription** | **kotlin.String** | Descripcion del presupuesto asociado |  [optional] |
| **budgetState** | [**inline**](#BudgetState) | Estado del presupuesto asociado |  [optional] |
| **savingGoalId** | **kotlin.Long** | Identificador de la meta de ahorro asociada |  [optional] |
| **savingGoalDescription** | **kotlin.String** | Descripcion de la meta de ahorro asociada |  [optional] |


<a id="Type"></a>
## Enum: type
| Name | Value |
| ---- | ----- |
| type | WARNING, SUCCESS, ERROR, INFO |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | SCHEDULED, SENT, READ, DISCARD |


<a id="BudgetState"></a>
## Enum: budgetState
| Name | Value |
| ---- | ----- |
| budgetState | ACTIVE, CANCELLED, DISABLED, FINALIZED |



