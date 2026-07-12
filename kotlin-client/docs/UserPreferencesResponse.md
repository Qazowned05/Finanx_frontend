
# UserPreferencesResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico |  [optional] |
| **timeZone** | [**inline**](#TimeZone) | Zona horaria preferida |  [optional] |
| **currencyCode** | **kotlin.String** | Codigo de moneda por defecto |  [optional] |
| **emailNotifications** | **kotlin.Boolean** | Notificaciones por email habilitadas |  [optional] |
| **fcmTokens** | **kotlin.collections.List&lt;kotlin.String&gt;** | Tokens FCM registrados |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha de creacion |  [optional] |
| **updatedAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Ultima actualizacion |  [optional] |


<a id="TimeZone"></a>
## Enum: timeZone
| Name | Value |
| ---- | ----- |
| timeZone | AMERICA_LIMA, AMERICA_BOGOTA, AMERICA_QUITO, AMERICA_CARACAS, AMERICA_LA_PAZ, AMERICA_ASUNCION, AMERICA_MONTEVIDEO, AMERICA_SANTIAGO, AMERICA_BUENOS_AIRES, AMERICA_GUATEMALA, AMERICA_COSTA_RICA, AMERICA_TEGUCIGALPA, AMERICA_EL_SALVADOR, AMERICA_MANAGUA, AMERICA_PANAMA, AMERICA_HAVANA, AMERICA_SANTO_DOMINGO, AMERICA_PUERTO_RICO, AMERICA_MEXICO_CITY, EUROPE_MADRID, AFRICA_CEUTA, ATLANTIC_CANARY, AMERICA_NEW_YORK, AMERICA_LOS_ANGELES |



