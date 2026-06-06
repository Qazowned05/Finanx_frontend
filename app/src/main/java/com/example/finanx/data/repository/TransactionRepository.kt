package com.example.finanx.data.repositories

import com.elias.finanx.dto.transaction.TransactionRequest
import com.elias.finanx.dto.transaction.TransactionResponse
import org.openapitools.client.apis.TransactionControllerApi
import retrofit2.Response

class TransactionRepository {
    private val api: TransactionControllerApi = TransactionControllerApi()

    suspend fun getTransactions(userId: Long): Response<List<TransactionResponse>> {
        return api.findAllActiveByUser(userId = userId)
    }

    suspend fun createTransaction(transaction: TransactionRequest): Response<TransactionResponse> {
        return api.create(request = transaction)
    }
}
