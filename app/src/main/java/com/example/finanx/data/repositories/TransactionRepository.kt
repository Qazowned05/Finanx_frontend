package com.example.finanx.data.repositories

import com.example.finanx.data.models.Transaction
import java.math.BigDecimal
import java.time.OffsetDateTime

object TransactionRepository {
    private val transactions = mutableListOf(
        Transaction(1, 1, 1, null, null, null, null, "CASH", "EXPENSE", BigDecimal("50.0"), "Compra en supermercado", OffsetDateTime.now().minusDays(1), true, null),
        Transaction(2, 1, 2, null, null, null, null, "CARD", "INCOME", BigDecimal("1500.0"), "Salario", OffsetDateTime.now().minusDays(1), true, null),
        Transaction(3, 1, 1, null, 1, null, null, "CASH", "EXPENSE", BigDecimal("25.0"), "Cine", OffsetDateTime.now().minusDays(2), true, null),
        Transaction(4, 1, 3, null, null, 1, null, "CARD", "EXPENSE", BigDecimal("10.0"), "Café", OffsetDateTime.now().minusDays(3), true, null),
        Transaction(5, 1, 2, null, null, null, null, "TRANSFER", "INCOME", BigDecimal("200.0"), "Venta online", OffsetDateTime.now().minusDays(4), true, null)
    )

    fun getTransactions(userId: Long): List<Transaction> {
        return transactions.filter { it.userId == userId && it.active }
    }

    fun createTransaction(transaction: Transaction): Transaction {
        val newTransaction = transaction.copy(id = (transactions.maxOfOrNull { it.id } ?: 0) + 1)
        transactions.add(newTransaction)
        return newTransaction
    }
}
