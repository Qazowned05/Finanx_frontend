
# UserResponse

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.Long** | Identificador unico del usuario |  [optional] |
| **name** | **kotlin.String** | Nombres del usuario |  [optional] |
| **lastname** | **kotlin.String** | Apellidos del usuario |  [optional] |
| **email** | **kotlin.String** | Correo electronico del usuario |  [optional] |
| **username** | **kotlin.String** | Nombre de usuario unico |  [optional] |
| **role** | [**Role**](Role.md) |  |  [optional] |
| **state** | [**UserState**](UserState.md) |  |  [optional] |
| **moneyBalance** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Balance monetario del usuario |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion del usuario |  [optional] |
| **disabledAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora en que el usuario fue deshabilitado, si aplica |  [optional] |
| **fullName** | **kotlin.String** | Nombre completo del usuario en formato concatenado |  [optional] |
| **preferences** | [**UserPreferencesResponse**](UserPreferencesResponse.md) |  |  [optional] |



