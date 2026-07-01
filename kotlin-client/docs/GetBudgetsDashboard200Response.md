
# GetBudgetsDashboard200Response

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **type** | **kotlin.String** |  |  |
| **periodResponse** | [**PeriodResponse**](PeriodResponse.md) | Periodo de referencia del dashboard |  [optional] |
| **title** | **kotlin.String** | Titulo descriptivo del dashboard |  [optional] |
| **createdAt** | [**java.time.LocalDateTime**](java.time.LocalDateTime.md) | Fecha y hora de creacion del dashboard |  [optional] |
| **insightSummary** | [**InsightSummary**](InsightSummary.md) | Resumen de insights generados para el periodo |  [optional] |
| **aggregate** | [**Aggregate**](Aggregate.md) | Agregado general con metricas calculadas |  [optional] |
| **byHealth** | [**kotlin.collections.List&lt;AggregateByBudgetHealth&gt;**](AggregateByBudgetHealth.md) | Lista de agregados por estado de salud del presupuesto |  [optional] |
| **byCategory** | [**kotlin.collections.List&lt;AggregateByBudgetExecutionResponse&gt;**](AggregateByBudgetExecutionResponse.md) | Lista de agregados por categoria de ejecucion presupuestal |  [optional] |
| **executionEvolution** | [**TimeLineBudgetExecutionResponse**](TimeLineBudgetExecutionResponse.md) | Linea de tiempo de la evolucion de ejecucion de presupuestos |  [optional] |
| **mostExecuted** | [**RankingCategoryResponse**](RankingCategoryResponse.md) | Ranking de categorias mas ejecutadas |  [optional] |
| **mostRemaining** | [**RankingCategoryResponse**](RankingCategoryResponse.md) | Ranking de categorias con mayor saldo restante |  [optional] |
| **totalBudgetLimitAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto limite total de todos los presupuestos |  [optional] |
| **totalBudgetSpentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto total gastado en todos los presupuestos |  [optional] |
| **totalBudgetRemainingAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Monto total restante en todos los presupuestos |  [optional] |
| **byState** | [**kotlin.collections.List&lt;AggregateBySavingGoalState&gt;**](AggregateBySavingGoalState.md) |  |  [optional] |
| **contributionEvolution** | [**TimeLineSavingGoalResponse**](TimeLineSavingGoalResponse.md) |  |  [optional] |
| **dailyContributions** | [**TimeLineLocalDate**](TimeLineLocalDate.md) |  |  [optional] |
| **closestToGoal** | [**RankingSavingGoalResponse**](RankingSavingGoalResponse.md) |  |  [optional] |
| **mostContributed** | [**RankingSavingGoalResponse**](RankingSavingGoalResponse.md) |  |  [optional] |
| **activeScheduledTransactionCount** | **kotlin.Long** |  |  [optional] |
| **activeCountByRecurrenceType** | **kotlin.collections.Map&lt;kotlin.String, kotlin.Long&gt;** |  |  [optional] |
| **projectionStart** | [**java.time.LocalDate**](java.time.LocalDate.md) |  |  [optional] |
| **projectionEnd** | [**java.time.LocalDate**](java.time.LocalDate.md) |  |  [optional] |
| **projectedIncomeAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **projectedSpentAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **projectedNetAmount** | [**java.math.BigDecimal**](java.math.BigDecimal.md) |  |  [optional] |
| **scheduledProjections** | [**kotlin.collections.List&lt;ScheduledProjection&gt;**](ScheduledProjection.md) |  |  [optional] |
| **insights** | [**kotlin.collections.List&lt;Insight&gt;**](Insight.md) |  |  [optional] |
| **balance** | [**java.math.BigDecimal**](java.math.BigDecimal.md) | Balance del periodo |  [optional] |
| **summaryByCategory** | [**RankingCategorySummary**](RankingCategorySummary.md) | Ranking de categorias con metricas resumidas |  [optional] |
| **summaryByPaymentMethod** | [**RankingPaymentMethod**](RankingPaymentMethod.md) | Ranking de metodos de pago con metricas resumidas |  [optional] |
| **summaryByType** | [**RankingTransactionType**](RankingTransactionType.md) | Ranking por tipo de transaccion con metricas resumidas |  [optional] |
| **dailyExpenses** | [**TimeLineTransactionResponse**](TimeLineTransactionResponse.md) | Linea de tiempo de gastos diarios |  [optional] |
| **dailyIncome** | [**TimeLineTransactionResponse**](TimeLineTransactionResponse.md) | Linea de tiempo de ingresos diarios |  [optional] |
| **topCategories** | [**RankingCategorySummary**](RankingCategorySummary.md) | Ranking de las categorias principales |  [optional] |
| **topSpentReasons** | [**RankingReasonSummary**](RankingReasonSummary.md) | Ranking de los principales motivos de gasto |  [optional] |
| **topIncomeReasons** | [**RankingReasonSummary**](RankingReasonSummary.md) | Ranking de los principales motivos de ingreso |  [optional] |



