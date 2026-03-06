package com.deallens.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deallens.ui.viewmodel.DealLensViewModel
import kotlin.math.max

@Composable
fun ResultScreen(
    viewModel: DealLensViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Ergebnisse", style = MaterialTheme.typography.headlineMedium)

        state.selectedBestOffer?.let { response ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(response.product_name, style = MaterialTheme.typography.titleLarge)
                    Text("Bester Shop: ${response.retailer}")
                    Text("Produkt: ${response.title}")
                    Text("Preis: ${response.price} ${response.currency}")
                    response.shipping_price?.let { Text("Versand: $it ${response.currency}") }
                    response.total_price?.let { Text("Gesamt: $it ${response.currency}") }
                }
            }
        }

        if (state.scanResults.isNotEmpty()) {
            Text("Scan-Treffer", style = MaterialTheme.typography.titleLarge)
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.scanResults) { result ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(result.detected_name, style = MaterialTheme.typography.titleMedium)
                            Text(if (result.matched_product_id != null) "Produkt erkannt" else "Kein Match")
                            result.matched_product_name?.let { matched ->
                                Text("Match: $matched")
                            }
                            result.detected_price?.let { Text("Prospektpreis: $it EUR") }
                            result.best_offer?.let { offer ->
                                Text("Online: ${offer.price} ${offer.currency} bei ${offer.retailer}")
                                val leaflet = result.detected_price ?: offer.price
                                val savings = max(0.0, leaflet - offer.price)
                                Text("Mögliche Ersparnis: $savings EUR")
                            }
                            Text("Trefferquote: ${(result.match_confidence * 100).toInt()}%")
                        }
                    }
                }
            }
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Zurück")
        }
    }
}
