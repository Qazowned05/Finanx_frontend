package com.example.finanx.ui.components

import androidx.compose.runtime.Composable
import com.example.finanx.data.model.CategoryOption
import com.example.finanx.data.model.TransactionType
import com.example.finanx.ui.screens.transaction.create.FormDropdown
import org.openapitools.client.models.CategoryResponse

@Composable
fun CategoryDropdown(
    categories: List<CategoryOption>,
    selectedCategoryId: Long?,
    enabled: Boolean,
    onCategorySelected: (Long?) -> Unit,
    label: String = "Categoría",
    placeholder: String = "Selecciona una categoría",
    supportingText: String? = null,
    isError: Boolean = false
) {
    FormDropdown(
        label = label,
        placeholder = placeholder,
        items = categories,
        selectedItem = categories.firstOrNull { it.id == selectedCategoryId },
        itemLabel = { it.label },
        enabled = enabled,
        onItemSelected = { onCategorySelected(it?.id) },
        supportingText = supportingText,
        isError = isError
    )
}

fun CategoryResponse.toCategoryOption(): CategoryOption? {
    val categoryId = id ?: return null
    val categoryType = type ?: return null

    return CategoryOption(
        id = categoryId,
        label = name ?: "Categoría $categoryId",
        type = TransactionType.valueOf(categoryType.value)
    )
}
