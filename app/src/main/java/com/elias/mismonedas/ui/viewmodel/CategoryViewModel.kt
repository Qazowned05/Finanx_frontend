package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import org.openapitools.client.models.PageResponseCategoryResponse
import org.openapitools.client.models.TransactionType
import javax.inject.Inject

private const val SEARCH_DEBOUNCE_MILLIS = 350L

sealed class CategoryState : RefreshableUiState {
    object Loading : CategoryState() {
        override val isRefreshing: Boolean = false
    }
    data class Success(
        val categories: List<CategoryResponse>,
        override val isRefreshing: Boolean = false,
        val page: Int = 0,
        val hasMore: Boolean = true,
        val isLoadingMore: Boolean = false
    ) : CategoryState()
    data class Error(val message: String) : CategoryState() {
        override val isRefreshing: Boolean = false
    }
}

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel() {

    var categoryState by mutableStateOf<CategoryState>(CategoryState.Loading)
        private set

    var actionSuccess by mutableStateOf<String?>(null)
        private set

    var query by mutableStateOf("")
        private set

    var typeFilter by mutableStateOf<TransactionType?>(null)
        private set

    private var searchJob: Job? = null

    fun fetchCategories(force: Boolean = false) {
        if (!force && categoryState is CategoryState.Success) return
        load()
    }

    private fun load() {
        viewModelScope.launch {
            val current = categoryState
            val hasData = current is CategoryState.Success && current.categories.isNotEmpty()
            categoryState = if (hasData) {
                (current as CategoryState.Success).copy(isRefreshing = true, isLoadingMore = false)
            } else {
                CategoryState.Loading
            }

            try {
                val response = repository.listCategoriesPage(
                    active = true,
                    type = typeFilter,
                    search = query.trim().ifBlank { null },
                    page = 0
                )
                categoryState = CategoryState.Success(
                    categories = response.items ?: emptyList(),
                    isRefreshing = false,
                    page = response.pagination?.page ?: 0,
                    hasMore = response.hasMorePages(),
                    isLoadingMore = false
                )
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun loadMore() {
        val current = categoryState as? CategoryState.Success ?: return
        if (!current.hasMore || current.isLoadingMore || current.isRefreshing) return

        viewModelScope.launch {
            categoryState = current.copy(isLoadingMore = true)
            try {
                val next = current.page + 1
                val response = repository.listCategoriesPage(
                    active = true,
                    type = typeFilter,
                    search = query.trim().ifBlank { null },
                    page = next
                )
                val latest = categoryState as? CategoryState.Success ?: return@launch
                categoryState = latest.copy(
                    categories = latest.categories + (response.items ?: emptyList()),
                    page = response.pagination?.page ?: next,
                    hasMore = response.hasMorePages(),
                    isLoadingMore = false
                )
            } catch (_: Exception) {
                val latest = categoryState as? CategoryState.Success
                if (latest != null) {
                    categoryState = latest.copy(isLoadingMore = false)
                }
            }
        }
    }

    fun onQueryChange(value: String) {
        if (query == value) return
        query = value
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_MILLIS)
            applyFilters()
        }
    }

    fun onTypeFilterChange(type: TransactionType?) {
        if (typeFilter == type) return
        typeFilter = type
        searchJob?.cancel()
        applyFilters()
    }

    private fun applyFilters() {
        categoryState = CategoryState.Loading
        load()
    }

    private fun PageResponseCategoryResponse.hasMorePages(): Boolean {
        val current = pagination?.page ?: 0
        val total = pagination?.totalPages ?: 1
        return current + 1 < total
    }

    fun createCategory(userId: Long, name: String, description: String, type: TransactionType) {
        viewModelScope.launch {
            try {
                val request = CategoryRequest(
                    userId = userId,
                    name = name,
                    description = description,
                    type = type
                )
                val response = repository.createCategory(request)
                if (response.isSuccessful) {
                    actionSuccess = "Categoría creada"
                    fetchCategories(force = true)
                } else {
                    categoryState = CategoryState.Error("Error al crear: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun updateCategory(userId: Long, categoryId: Long, name: String, description: String, type: TransactionType) {
        viewModelScope.launch {
            try {
                val request = CategoryRequest(
                    userId = userId,
                    name = name,
                    description = description,
                    type = type
                )
                val response = repository.updateCategory(categoryId, request)
                if (response.isSuccessful) {
                    actionSuccess = "Categoría actualizada"
                    fetchCategories(force = true)
                } else {
                    categoryState = CategoryState.Error("Error al actualizar: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun disableCategory(userId: Long, categoryId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.disableCategory(categoryId)
                if (response.isSuccessful) {
                    actionSuccess = "Categoría desactivada"
                    fetchCategories(force = true)
                } else {
                    categoryState = CategoryState.Error("Error al desactivar: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun clearActionSuccess() {
        actionSuccess = null
    }
}

