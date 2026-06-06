package com.example.finanx.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanx.data.api.RetrofitClient
import com.example.finanx.data.repository.CategoryRepository
import kotlinx.coroutines.launch
import org.openapitools.client.models.CategoryRequest
import org.openapitools.client.models.CategoryResponse

sealed class CategoryState {
    object Loading : CategoryState()
    data class Success(val categories: List<CategoryResponse>) : CategoryState()
    data class Error(val message: String) : CategoryState()
}

class CategoryViewModel : ViewModel() {
    private val repository = CategoryRepository(RetrofitClient.categoryApi)

    var categoryState by mutableStateOf<CategoryState>(CategoryState.Loading)
        private set

    var actionSuccess by mutableStateOf<String?>(null)
        private set

    fun fetchCategories(userId: Long) {
        viewModelScope.launch {
            categoryState = CategoryState.Loading
            try {
                val response = repository.getCategories(userId)
                if (response.isSuccessful && response.body() != null) {
                    categoryState = CategoryState.Success(response.body()!!)
                } else {
                    categoryState = CategoryState.Error("Error al cargar categorías: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun createCategory(userId: Long, name: String, description: String) {
        viewModelScope.launch {
            try {
                val request = CategoryRequest(
                    userId = userId,
                    name = name,
                    description = description
                )
                val response = repository.createCategory(request)
                if (response.isSuccessful) {
                    actionSuccess = "Categoría creada"
                    fetchCategories(userId)
                } else {
                    categoryState = CategoryState.Error("Error al crear: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun updateCategory(userId: Long, categoryId: Long, name: String, description: String) {
        viewModelScope.launch {
            try {
                val request = CategoryRequest(
                    userId = userId,
                    name = name,
                    description = description
                )
                val response = repository.updateCategory(categoryId, request)
                if (response.isSuccessful) {
                    actionSuccess = "Categoría actualizada"
                    fetchCategories(userId)
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
                    fetchCategories(userId)
                } else {
                    categoryState = CategoryState.Error("Error al desactivar: ${response.code()}")
                }
            } catch (e: Exception) {
                categoryState = CategoryState.Error("Error de red: ${e.message}")
            }
        }
    }

    fun activateCategory(userId: Long, categoryId: Long) {
        viewModelScope.launch {
            try {
                val response = repository.activateCategory(categoryId)
                if (response.isSuccessful) {
                    actionSuccess = "Categoría activada"
                    fetchCategories(userId)
                } else {
                    categoryState = CategoryState.Error("Error al activar: ${response.code()}")
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

