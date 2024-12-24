package com.example.ucp2_20220140002.ui.view.jadwal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2_20220140002.data.entity.Jadwal
import com.example.ucp2_20220140002.ui.costumwidget.TopAppBar
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.HomeJadwalViewModel
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.HomeUiState
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.PenyediaJadwalViewModel

@Composable
fun HomeJadwalView(
    onDetailJadwal: (String) -> Unit, // Terima ID jadwal untuk navigasi ke detail
    onBack: () -> Unit,
    onNavigateToInsertJadwal: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeJadwalViewModel = viewModel(factory = PenyediaJadwalViewModel.FactoryJadwal)
) {
    val state by viewModel.homeJdwlUIState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Daftar Jadwal"
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToInsertJadwal) {
                Text("+") // Tombol untuk menambah jadwal baru
            }
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.isError -> {
                    Text(
                        text = "Error: ${state.errorMessage}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                state.listJadwal.isNotEmpty() -> {
                    state.listJadwal.forEach { jadwal ->
                        JadwalItem(
                            jadwal = jadwal,
                            onClick = { onDetailJadwal(jadwal.idPasien) } // Panggil navigasi ke detail
                        )
                    }
                }
                else -> {
                    Text(
                        text = "Tidak ada jadwal yang tersedia.",
                        color = Color.Gray,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
fun JadwalItem(
    jadwal: Jadwal,
    onClick: (String) -> Unit // Fungsi onClick untuk navigasi
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(jadwal.idPasien) } // Tambahkan klik
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Pasien: ${jadwal.namaPasien}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Dokter: ${jadwal.namaDokter}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Tanggal: ${jadwal.tanggalKonsultasi}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Status: ${jadwal.Status}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

