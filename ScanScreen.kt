package com.dealz.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dealz.ui.viewmodel.DealZViewModel

@Composable
fun ScanScreen(
    viewModel: DealZViewModel,
    onBack: () -> Unit,
    onFinished: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Prospekt-Scan", style = MaterialTheme.typography.headlineMedium)
        Text("Die App kann sofort mit eingebauten Demo-Daten laufen. Sobald dein Backend online ist, werden dieselben Buttons echte API-Requests nutzen.")

        Button(
            onClick = { viewModel.runDemoScan() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Demo-Scan starten")
        }

        if (state.loading) {
            CircularProgressIndicator()
        }

        if (state.scanResults.isNotEmpty()) {
            LaunchedEffect(state.scanResults.size) {
                onFinished()
            }
        }

        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Zurück")
        }
    }
}
