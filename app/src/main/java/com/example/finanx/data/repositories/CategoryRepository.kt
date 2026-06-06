package com.example.finanx.data.repositories

import com.example.finanx.data.models.Category

object CategoryRepository {

    private val categories = listOf(
        Category(1, 1, "Alimentación", "EXPENSE", null),
        Category(2, 1, "Transporte", "EXPENSE", null),
        Category(3, 1, "Ocio", "EXPENSE", null),
        Category(4, 1, "Salario", "INCOME", null),
        Category(5, 1, "Ventas", "INCOME", null),
        Category(6, 1, "Regalos", "INCOME", null)
    )

    fun getCategories(userId: Long): List<Category> {
        return categories.filter { it.userId == userId }
    }
}
