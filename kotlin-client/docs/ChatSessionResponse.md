
# ChatSessionResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | ID de la sesion |  [optional] |
| **uuid** | **kotlin.String** | UUID de la sesion |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha de creacion de la sesion |  [optional] |
| **endedAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha de finalizacion de la sesion |  [optional] |
| **state** | [**inline**](#State) | Estado de la sesion |  [optional] |
| **userId** | **kotlin.Long** | ID de usuario asociado a la sesion |  [optional] |
| **history** | [**kotlin.collections.List&lt;ChatHistoryResponse&gt;**](ChatHistoryResponse.md) | Historial de mensajes de la sesion |  [optional] |


<a id="State"></a>
## Enum: state
| Name | Value |
| ---- | ----- |
| state | ACTIVE, EXPIRED, CLOSED |



