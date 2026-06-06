package com.example.finanx.data.repository

import com.example.finanx.data.models.Transaction
import org.openapitools.client.apis.TransactionControllerApi
import retrofit2.Response

class TransactionRepository(private val api: TransactionControllerApi) {

    suspend fun getTransactions(userId: Long): Response<List<Transaction>> {
        return Response.success(emptyList())
    }


    suspend fun deleteTransaction(transactionId: Long): Response<Unit> {
        return Response.success(Unit) 
    }
}