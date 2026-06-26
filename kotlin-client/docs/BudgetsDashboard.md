
# BudgetsDashboard

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **byHealth** | [**kotlin.collections.List&lt;AggregateByBudgetHealth&gt;**](AggregateByBudgetHealth.md) | Lista de agregados por estado de salud del presupuesto |  [optional] |
| **byCategory** | [**kotlin.collections.List&lt;AggregateByBudgetExecutionResponse&gt;**](AggregateByBudgetExecutionResponse.md) | Lista de agregados por categoria de ejecucion presupuestal |  [optional] |
| **executionEvolution** | [**TimeLineBudgetExecutionResponse**](TimeLineBudgetExecutionResponse.md) | Linea de tiempo de la evolucion de ejecucion de presupuestos |  [optional] |
| **mostExecuted** | [**RankingCategoryResponse**](RankingCategoryResponse.md) | Ranking de categorias mas ejecutadas |  [optional] |
| **mostRemaining** | [**RankingCategoryResponse**](RankingCategoryResponse.md) | Ranking de categorias con mayor saldo restante |  [optional] |
| **totalBudgetLimitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto limite total de todos los presupuestos |  [optional] |
| **totalBudgetSpentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto total gastado en todos los presupuestos |  [optional] |
| **totalBudgetRemainingAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto total restante en todos los presupuestos |  [optional] |



