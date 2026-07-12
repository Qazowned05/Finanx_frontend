package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse
import javax.inject.Inject

sealed class CategoryState : com.elias.mismonedas.ui.viewmodel.RefreshableUiState {
    object Loading : CategoryState() {
        override val isRefreshing: Boolean = false
    }
    data class Success(val categories: List<CategoryResponse>, override val isRefreshing: Boolean = false) : CategoryState()
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

    fun fetchCategories(userId: Long, force: Boolean = false) {
        if (!force && categoryState is CategoryState.Success) return
        viewModelScope.launch {
            val hasData = categoryState is CategoryState.Success && (categoryState as CategoryState.Success).categories.isNotEmpty()
            categoryState = if (hasData) (categoryState as CategoryState.Success).copy(isRefreshing = true)
            else CategoryState.Loading

            try {
                val response = repository.getActiveCategories(userId)
                if (response.isSuccessful && response.body() != null) {
                    categoryState = CategoryState.Success(response.body()!!, isRefreshing = false)
                } else {
                    categoryState = CategoryState.Error("Error al cargar categorías: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createCategory(userId: Long, name: String, description: String, type: CategoryRequest.Type) {
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
                    fetchCategories(userId, force = true)
                } else {
                    categoryState = CategoryState.Error("Error al crear: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun updateCategory(userId: Long, categoryId: Long, name: String, description: String, type: CategoryRequest.Type) {
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
                    fetchCategories(userId, force = true)
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
                    fetchCategories(userId, force = true)
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

