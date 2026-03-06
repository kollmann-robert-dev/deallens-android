package com.deallens.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deallens.data.model.BestOfferResponse
import com.deallens.data.model.ProductItem
import com.deallens.data.model.ScanMatchItem
import com.deallens.data.repository.DealLensRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UiState(
    val query: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val info: String? = "Offline-Demo aktiv. Mit Backend werden Live-Daten geladen.",
    val products: List<ProductItem> = emptyList(),
    val selectedBestOffer: BestOfferResponse? = null,
    val scanResults: List<ScanMatchItem> = emptyList()
)

class DealLensViewModel(
    private val repository: DealLensRepository = DealLensRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateQuery(value: String) {
        _uiState.value = _uiState.value.copy(query = value)
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(error = null, info = null)
    }

    fun searchProducts() {
        val query = _uiState.value.query.trim()
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Bitte gib einen Produktnamen ein.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            runCatching { repository.searchProducts(query) }
                .onSuccess { products ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        products = products,
                        selectedBestOffer = null,
                        scanResults = emptyList(),
                        info = if (products.isEmpty()) "Keine Treffer gefunden." else _uiState.value.info
                    )
                }
                .onFailure { ex ->
                    _uiState.value = _uiState.value.copy(loading = false, error = ex.message)
                }
        }
    }

    fun loadBestOffer(productId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null)
            runCatching { repository.getBestOffer(productId) }
                .onSuccess { offer ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        selectedBestOffer = offer,
                        scanResults = emptyList()
                    )
                }
                .onFailure { ex ->
                    _uiState.value = _uiState.value.copy(loading = false, error = ex.message)
                }
        }
    }

    fun runDemoScan() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = null, selectedBestOffer = null)
            runCatching { repository.submitDemoScan() }
                .onSuccess { results ->
                    _uiState.value = _uiState.value.copy(loading = false, scanResults = results)
                }
                .onFailure { ex ->
                    _uiState.value = _uiState.value.copy(loading = false, error = ex.message)
                }
        }
    }
}
