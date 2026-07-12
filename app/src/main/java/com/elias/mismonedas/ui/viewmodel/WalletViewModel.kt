package com.elias.mismonedas.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.mismonedas.data.repository.WalletRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.openapitools.client.models.WalletResponse
import javax.inject.Inject

sealed class WalletListState : com.elias.mismonedas.ui.viewmodel.RefreshableUiState {
    object Loading : WalletListState() { override val isRefreshing: Boolean = false }
    data class Success(val wallets: List<WalletResponse>, override val isRefreshing: Boolean = false) : WalletListState()
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
                (walletListState as WalletListState.Success).copy(isRefreshing = true)
            else WalletListState.Loading
            try {
                val wallets = repository.listByUser(userId)
                walletListState = WalletListState.Success(wallets, isRefreshing = false)
            } catch (e: Exception) {
                walletListState = WalletListState.Error("Error: ${e.message}")
            }
        }
    }

    fun createWallet(userId: Long, currencyCode: String) {
        viewModelScope.launch {
            actionState = WalletActionState.Loading
            try {
                repository.create(userId, currencyCode)
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
                repository.update(userId, walletId, currencyCode, primary = true)
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
                repository.disable(userId, walletId)
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
}
