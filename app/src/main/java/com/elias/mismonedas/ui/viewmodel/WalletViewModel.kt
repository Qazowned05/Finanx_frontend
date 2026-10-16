package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.PageResponseWalletResponse
import org.openapitools.client.models.WalletResponse
import javax.inject.Inject

sealed class WalletListState : RefreshableUiState {
    object Loading : WalletListState() { override val isRefreshing: Boolean = false }
    data class Success(
        val wallets: List<WalletResponse>,
        override val isRefreshing: Boolean = false,
        val page: Int = 0,
        val hasMore: Boolean = true,
        val isLoadingMore: Boolean = false
    ) : WalletListState()
    data class Error(val message: String) : WalletListState() { override val isRefreshing: Boolean = false }
}

sealed class WalletActionState {
    object Idle : WalletActionState()
    object Loading : WalletActionState()
    object Success : WalletActionState()
    data class Error(val message: String) : WalletActionState()
}

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val repository: WalletRepository
) : ViewModel() {

    var walletListState by mutableStateOf<WalletListState>(WalletListState.Loading)
        private set

    var actionState by mutableStateOf<WalletActionState>(WalletActionState.Idle)
        private set

    fun loadWallets(userId: Long) {
        viewModelScope.launch {
            val hasData = walletListState is WalletListState.Success &&
                (walletListState as WalletListState.Success).wallets.isNotEmpty()
            walletListState = if (hasData)
                (walletListState as WalletListState.Success).copy(
                    isRefreshing = true,
                    isLoadingMore = false
                )
            else WalletListState.Loading
            try {
                val page = repository.listPage(page = 0)
                walletListState = WalletListState.Success(
                    wallets = page.items ?: emptyList(),
                    isRefreshing = false,
                    page = page.pagination?.page ?: 0,
                    hasMore = page.hasMorePages(),
                    isLoadingMore = false
                )
            } catch (e: Exception) {
                walletListState = WalletListState.Error("Error: ${e.message}")
            }
        }
    }

    fun loadMore(userId: Long) {
        val current = walletListState as? WalletListState.Success ?: return
        if (!current.hasMore || current.isLoadingMore || current.isRefreshing) return

        viewModelScope.launch {
            walletListState = current.copy(isLoadingMore = true)
            try {
                val next = current.page + 1
                val page = repository.listPage(page = next)
                val latest = walletListState as? WalletListState.Success ?: return@launch
                walletListState = latest.copy(
                    wallets = latest.wallets + (page.items ?: emptyList()),
                    page = page.pagination?.page ?: next,
                    hasMore = page.hasMorePages(),
                    isLoadingMore = false
                )
            } catch (e: Exception) {
                val latest = walletListState as? WalletListState.Success ?: return@launch
                walletListState = latest.copy(isLoadingMore = false)
            }
        }
    }

    fun createWallet(userId: Long, currencyCode: String) {
        viewModelScope.launch {
            actionState = WalletActionState.Loading
            try {
                repository.create(currencyCode)
                actionState = WalletActionState.Success
                loadWallets(userId)
            } catch (e: Exception) {
                actionState = WalletActionState.Error("Error al crear: ${e.message}")
            }
        }
    }

    fun setPrimary(userId: Long, walletId: Long, currencyCode: String) {
        viewModelScope.launch {
            actionState = WalletActionState.Loading
            try {
                repository.update(walletId, currencyCode, primary = true)
                actionState = WalletActionState.Success
                loadWallets(userId)
            } catch (e: Exception) {
                actionState = WalletActionState.Error("Error al actualizar: ${e.message}")
            }
        }
    }

    fun disableWallet(userId: Long, walletId: Long) {
        viewModelScope.launch {
            actionState = WalletActionState.Loading
            try {
                repository.disable(walletId)
                actionState = WalletActionState.Success
                loadWallets(userId)
            } catch (e: Exception) {
                actionState = WalletActionState.Error("Error al deshabilitar: ${e.message}")
            }
        }
    }

    fun resetActionState() {
        actionState = WalletActionState.Idle
    }

    private fun PageResponseWalletResponse.hasMorePages(): Boolean {
        val current = pagination?.page ?: 0
        val total = pagination?.totalPages ?: 1
        return current + 1 < total
    }
}
