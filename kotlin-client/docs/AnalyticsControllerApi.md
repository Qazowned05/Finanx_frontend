# AnalyticsControllerApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**dashboard**](AnalyticsControllerApi.md#dashboard) | **GET** api/analytics/dashboard/{userId} |  |
| [**getActiveScheduledCountByRecurrenceType**](AnalyticsControllerApi.md#getActiveScheduledCountByRecurrenceType) | **GET** api/analytics/scheduled/active-count-by-recurrence |  |
| [**getActiveScheduledTransactionCount**](AnalyticsControllerApi.md#getActiveScheduledTransactionCount) | **GET** api/analytics/scheduled/active-count |  |
| [**getBudgetExecutions**](AnalyticsControllerApi.md#getBudgetExecutions) | **POST** api/analytics/budgets/executions |  |
| [**getBudgetInsights**](AnalyticsControllerApi.md#getBudgetInsights) | **POST** api/analytics/budgets/insights |  |
| [**getBudgetOverview**](AnalyticsControllerApi.md#getBudgetOverview) | **POST** api/analytics/budgets/overview |  |
| [**getCategoryAggregates**](AnalyticsControllerApi.md#getCategoryAggregates) | **POST** api/analytics/categories/aggregates |  |
| [**getCategoryOverview**](AnalyticsControllerApi.md#getCategoryOverview) | **POST** api/analytics/categories/overview |  |
| [**getDailyNetSeries**](AnalyticsControllerApi.md#getDailyNetSeries) | **POST** api/analytics/transactions/daily-net-series |  |
| [**getExceededBudgets**](AnalyticsControllerApi.md#getExceededBudgets) | **POST** api/analytics/budgets/exceeded |  |
| [**getMostUsedPaymentMethod**](AnalyticsControllerApi.md#getMostUsedPaymentMethod) | **POST** api/analytics/transactions/most-used-payment-method |  |
| [**getNearLimitBudgets**](AnalyticsControllerApi.md#getNearLimitBudgets) | **POST** api/analytics/budgets/near-limit |  |
| [**getReasonOverview**](AnalyticsControllerApi.md#getReasonOverview) | **POST** api/analytics/reasons/overview |  |
| [**getScheduledInsights**](AnalyticsControllerApi.md#getScheduledInsights) | **POST** api/analytics/scheduled/insights |  |
| [**getScheduledOverview**](AnalyticsControllerApi.md#getScheduledOverview) | **POST** api/analytics/scheduled/overview |  |
| [**getScheduledProjections**](AnalyticsControllerApi.md#getScheduledProjections) | **POST** api/analytics/scheduled/projections |  |
| [**getTopCategories**](AnalyticsControllerApi.md#getTopCategories) | **POST** api/analytics/categories/top |  |
| [**getTopPaymentMethods**](AnalyticsControllerApi.md#getTopPaymentMethods) | **POST** api/analytics/transactions/top-payment-methods |  |
| [**getTopReasons**](AnalyticsControllerApi.md#getTopReasons) | **POST** api/analytics/reasons/top |  |
| [**getTotalAmountByTransactionType**](AnalyticsControllerApi.md#getTotalAmountByTransactionType) | **POST** api/analytics/categories/total-amount-by-type |  |
| [**getTransactionAggregatesByPaymentMethod**](AnalyticsControllerApi.md#getTransactionAggregatesByPaymentMethod) | **POST** api/analytics/transactions/aggregates/payment-method |  |
| [**getTransactionAggregatesByType**](AnalyticsControllerApi.md#getTransactionAggregatesByType) | **POST** api/analytics/transactions/aggregates/type |  |
| [**getTransactionInsights**](AnalyticsControllerApi.md#getTransactionInsights) | **POST** api/analytics/transactions/insights |  |
| [**getTransactionOverview**](AnalyticsControllerApi.md#getTransactionOverview) | **POST** api/analytics/transactions/overview |  |
| [**getUserInsights**](AnalyticsControllerApi.md#getUserInsights) | **POST** api/analytics/user/insights |  |
| [**makeProjection**](AnalyticsControllerApi.md#makeProjection) | **POST** api/analytics/scheduled/projection |  |





### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val start : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 
val end : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 

launch(Dispatchers.IO) {
    val result : AnalyticsDashboard = webService.dashboard(userId, start, end)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **start** | **java.time.OffsetDateTime**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **end** | **java.time.OffsetDateTime**|  | |

### Return type

[**AnalyticsDashboard**](AnalyticsDashboard.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.Map<kotlin.String, kotlin.Long> = webService.getActiveScheduledCountByRecurrenceType(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

**kotlin.collections.Map&lt;kotlin.String, kotlin.Long&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 

launch(Dispatchers.IO) {
    val result : kotlin.Long = webService.getActiveScheduledTransactionCount(userId)
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **userId** | **kotlin.Long**|  | |

### Return type

**kotlin.Long**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetAnalytics> = webService.getBudgetExecutions(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;BudgetAnalytics&gt;**](BudgetAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Insight> = webService.getBudgetInsights(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;Insight&gt;**](Insight.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : BudgetAnalytics = webService.getBudgetOverview(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**BudgetAnalytics**](BudgetAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<CategoryAggregate> = webService.getCategoryAggregates(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;CategoryAggregate&gt;**](CategoryAggregate.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : CategoryAnalytics = webService.getCategoryOverview(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**CategoryAnalytics**](CategoryAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<TimeSeriesPoint> = webService.getDailyNetSeries(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;TimeSeriesPoint&gt;**](TimeSeriesPoint.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetAnalytics> = webService.getExceededBudgets(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;BudgetAnalytics&gt;**](BudgetAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.String = webService.getMostUsedPaymentMethod(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

**kotlin.String**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<BudgetAnalytics> = webService.getNearLimitBudgets(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;BudgetAnalytics&gt;**](BudgetAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val topN : kotlin.Int = 56 // kotlin.Int | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : ReasonAnalytics = webService.getReasonOverview(userId, topN, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **topN** | **kotlin.Int**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**ReasonAnalytics**](ReasonAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val projectionStart : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 
val projectionEnd : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Insight> = webService.getScheduledInsights(userId, projectionStart, projectionEnd)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **projectionStart** | **java.time.OffsetDateTime**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **projectionEnd** | **java.time.OffsetDateTime**|  | |

### Return type

[**kotlin.collections.List&lt;Insight&gt;**](Insight.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val projectionStart : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 
val projectionEnd : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 

launch(Dispatchers.IO) {
    val result : TransactionScheduleAnalytics = webService.getScheduledOverview(userId, projectionStart, projectionEnd)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **projectionStart** | **java.time.OffsetDateTime**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **projectionEnd** | **java.time.OffsetDateTime**|  | |

### Return type

[**TransactionScheduleAnalytics**](TransactionScheduleAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val projectionStart : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 
val projectionEnd : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<ScheduledProjection> = webService.getScheduledProjections(userId, projectionStart, projectionEnd)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **projectionStart** | **java.time.OffsetDateTime**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **projectionEnd** | **java.time.OffsetDateTime**|  | |

### Return type

[**kotlin.collections.List&lt;ScheduledProjection&gt;**](ScheduledProjection.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val topN : kotlin.Int = 56 // kotlin.Int | 
val type : kotlin.String = type_example // kotlin.String | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<RankingItemCategorySummary> = webService.getTopCategories(userId, topN, type, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **topN** | **kotlin.Int**|  | |
| **type** | **kotlin.String**|  | [enum: SPENT, INCOME] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;RankingItemCategorySummary&gt;**](RankingItemCategorySummary.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val topN : kotlin.Int = 56 // kotlin.Int | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<RankingItemPaymentMethod> = webService.getTopPaymentMethods(userId, topN, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **topN** | **kotlin.Int**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;RankingItemPaymentMethod&gt;**](RankingItemPaymentMethod.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val topN : kotlin.Int = 56 // kotlin.Int | 
val type : kotlin.String = type_example // kotlin.String | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<RankingItemReasonSummary> = webService.getTopReasons(userId, topN, type, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **topN** | **kotlin.Int**|  | |
| **type** | **kotlin.String**|  | [enum: SPENT, INCOME] |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;RankingItemReasonSummary&gt;**](RankingItemReasonSummary.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.Map<kotlin.String, java.math.BigDecimal> = webService.getTotalAmountByTransactionType(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.Map&lt;kotlin.String, java.math.BigDecimal&gt;**](java.math.BigDecimal.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.Map<kotlin.String, TransactionAggregate> = webService.getTransactionAggregatesByPaymentMethod(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.Map&lt;kotlin.String, TransactionAggregate&gt;**](TransactionAggregate.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.Map<kotlin.String, TransactionAggregate> = webService.getTransactionAggregatesByType(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.Map&lt;kotlin.String, TransactionAggregate&gt;**](TransactionAggregate.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Insight> = webService.getTransactionInsights(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;Insight&gt;**](Insight.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : TransactionAnalytics = webService.getTransactionOverview(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**TransactionAnalytics**](TransactionAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val analyticsPeriod : AnalyticsPeriod =  // AnalyticsPeriod | 

launch(Dispatchers.IO) {
    val result : kotlin.collections.List<Insight> = webService.getUserInsights(userId, analyticsPeriod)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **analyticsPeriod** | [**AnalyticsPeriod**](AnalyticsPeriod.md)|  | |

### Return type

[**kotlin.collections.List&lt;Insight&gt;**](Insight.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*




### Example
```kotlin
// Import classes:
//import org.openapitools.client.*
//import org.openapitools.client.infrastructure.*
//import org.openapitools.client.models.*

val apiClient = ApiClient()
val webService = apiClient.createWebservice(AnalyticsControllerApi::class.java)
val userId : kotlin.Long = 789 // kotlin.Long | 
val projectionStart : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 
val projectionEnd : java.time.OffsetDateTime = 2013-10-20T19:20:30+01:00 // java.time.OffsetDateTime | 
val type : kotlin.String = type_example // kotlin.String | 

launch(Dispatchers.IO) {
    val result : TransactionScheduleAnalytics = webService.makeProjection(userId, projectionStart, projectionEnd, type)
}
```

### Parameters
| **userId** | **kotlin.Long**|  | |
| **projectionStart** | **java.time.OffsetDateTime**|  | |
| **projectionEnd** | **java.time.OffsetDateTime**|  | |
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **type** | **kotlin.String**|  | [enum: SPENT, INCOME] |

### Return type

[**TransactionScheduleAnalytics**](TransactionScheduleAnalytics.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

