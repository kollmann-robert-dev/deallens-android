package com.deallens.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deallens.ui.viewmodel.DealLensViewModel

@Composable
fun HomeScreen(
    viewModel: DealLensViewModel,
    onOpenScan: () -> Unit,
    onOpenResults: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("DealLens", style = MaterialTheme.typography.headlineMedium)
        Text("Produkte suchen oder Prospekt-Scan starten")

        state.info?.let {
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(it, modifier = Modifier.padding(16.dp))
            }
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.query,
            onValueChange = viewModel::updateQuery,
            label = { Text("Produktname") }
        )

        Button(onClick = { viewModel.searchProducts() }, modifier = Modifier.fillMaxWidth()) {
            Text("Produkte suchen")
        }

        Button(onClick = onOpenScan, modifier = Modifier.fillMaxWidth()) {
            Text("Prospekt-Scan öffnen")
        }

        state.error?.let {
            Text("Fehler: $it", color = MaterialTheme.colorScheme.error)
        }

        if (state.loading) {
            CircularProgressIndicator()
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.products) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.loadBestOffer(product.id)
                            onOpenResults()
                        }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(product.name, style = MaterialTheme.typography.titleMedium)
                        Text(listOfNotNull(product.brand, product.variant).joinToString(" • "))
                        val size = listOfNotNull(product.size_value?.toString(), product.size_unit).joinToString(" ")
                        if (size.isNotBlank()) Text(size)
                        product.source?.let { Text("Quelle: $it") }
                    }
                }
            }
        }
    }
}
