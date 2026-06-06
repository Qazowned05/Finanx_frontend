package com.example.finanx.data.repository

import org.openapitools.client.apis.TransactionControllerApi
import org.openapitools.client.models.TransactionRequest
import org.openapitools.client.models.TransactionResponse
import retrofit2.Response

class TransactionRepository {
    private val api: TransactionControllerApi = TransactionControllerApi()

    suspend fun getTransactions(userId: Long): Response<List<TransactionResponse>> {
        return api.findAllActiveByUser(userId = userId)
    }

    suspend fun createTransaction(transaction: TransactionRequest): Response<TransactionResponse> {
        // Corregido: el parámetro se llama 'transactionRequest'
        return api.create(transactionRequest = transaction)
    }
}
