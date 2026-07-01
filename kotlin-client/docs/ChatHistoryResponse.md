
# ChatHistoryResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico del historial |  [optional] |
| **turnNumber** | **kotlin.Int** | Numero de turno dentro de la sesion |  [optional] |
| **role** | [**inline**](#Role) | Rol del mensaje |  [optional] |
| **message** | **kotlin.String** | Contenido del mensaje |  [optional] |
| **tokensUsed** | **kotlin.Int** | Cantidad de tokens usados |  [optional] |
| **model** | **kotlin.String** | Modelo utilizado |  [optional] |
| **finishReason** | **kotlin.String** | Motivo de finalizacion |  [optional] |
| **safetyRatings** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Ratings de seguridad en formato JSON |  [optional] |
| **latencyMs** | **kotlin.Long** | Latencia en milisegundos |  [optional] |
| **metadata** | [**kotlin.collections.Map&lt;kotlin.String, kotlin.Any&gt;**](kotlin.Any.md) | Metadatos adicionales en formato JSON |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha de creacion del mensaje |  [optional] |
| **sessionUUID** | **kotlin.String** | UUID de la sesion |  [optional] |
| **sessionId** | **kotlin.Long** | ID de la sesion |  [optional] |


<a id="Role"></a>
## Enum: role
| Name | Value |
| ---- | ----- |
| role | USER, AI |



