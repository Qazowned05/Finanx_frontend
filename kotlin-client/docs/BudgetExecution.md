
# BudgetExecution

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **budgetId** | **kotlin.Long** |  |  [optional] |
| **budgetDescription** | **kotlin.String** |  |  [optional] |
| **budgetState** | [**inline**](#BudgetState) |  |  [optional] |
| **notificationEnabled** | **kotlin.Boolean** |  |  [optional] |
| **alertThresholdPercentage** | **kotlin.Int** |  |  [optional] |
| **budgetStartDate** | [**java.time.LocalDate**](java.time.LocalDate.md) |  |  [optional] |
| **budgetEndDate** | [**java.time.LocalDate**](java.time.LocalDate.md) |  |  [optional] |
| **category** | [**CategoryDTO**](CategoryDTO.md) |  |  [optional] |
| **budgetLimitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **spentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **remainingAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **executionPercentage** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **budgetHealth** | [**inline**](#BudgetHealth) |  |  [optional] |


<a id="BudgetState"></a>
## Enum: budgetState
| Name | Value |
| ---- | ----- |
| budgetState | ACTIVE, CANCELLED, DISABLED, FINALIZED |


<a id="BudgetHealth"></a>
## Enum: budgetHealth
| Name | Value |
| ---- | ----- |
| budgetHealth | OK, NEAR_LIMIT, EXCEEDED, INACTIVE |



