package com.example.ucp2_20220140002.ui.view.dokter

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.ui.costumwidget.TopAppBar
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DetailDokterViewModel
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DetailUiState
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DokterUIState
import com.example.ucp2_20220140002.ui.viewmodel.dokter.toDokterEntity

@Composable
fun DetailDokterView(
    modifier: Modifier = Modifier,
    viewModel: DetailDokterViewModel = viewModel(),
    onBack: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.padding(top = 16.dp),
        topBar = {
            TopAppBar(
                judul = "Detail Dokter",
                showBackButton = true,
                onBack = onBack,
            )
        }
    ) { innerPadding ->
        val detailUiState by viewModel.detailUiState.collectAsState()

        BodyDetailDokter(
            modifier = Modifier.padding(innerPadding),
            detailUiState = detailUiState
        )
    }
}

@Composable
fun BodyDetailDokter(
    modifier: Modifier = Modifier,
    detailUiState: DetailUiState = DetailUiState()
) {
    when {
        detailUiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        detailUiState.isUiEventNotEmpty -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailDokter(
                    dokter = detailUiState.detailUiEvent.toDokterEntity(),
                    modifier = Modifier
                )
            }
        }

        detailUiState.isUiEventEmpty -> {
            Box(
                modifier = modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun ItemDetailDokter(
    modifier: Modifier = Modifier,
    dokter: Dokter
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ComponentDetailDokter(judul = "ID Dokter", isinya = dokter.id)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailDokter(judul = "Nama", isinya = dokter.nama)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailDokter(judul = "Spesialis", isinya = dokter.spesialis)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailDokter(judul = "Klinik", isinya = dokter.klinik)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailDokter(judul = "No HP", isinya = dokter.noHp)
            Spacer(modifier = Modifier.padding(4.dp))

            ComponentDetailDokter(judul = "Jam Kerja", isinya = dokter.jamKerja)
        }
    }
}

@Composable
fun ComponentDetailDokter(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul : ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya, fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
