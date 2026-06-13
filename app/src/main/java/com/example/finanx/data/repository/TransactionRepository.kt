package com.example.finanx.data.repository

import com.example.finanx.data.api.RetrofitClient
import org.openapitools.client.apis.TransactionControllerApi
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import retrofit2.Response

class TransactionRepository(
    private val api: TransactionControllerApi = RetrofitClient.transactionApi
) {

    suspend fun getTransactions(userId: Long): Response<List<TransactionResponse>> {
        return api.findAllActiveByUser(userId = userId)
    }

    suspend fun createTransaction(transaction: TransactionRequest): Response<TransactionResponse> {
        return api.create(transactionRequest = transaction)
    }
}
