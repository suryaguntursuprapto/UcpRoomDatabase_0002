package com.example.ucp2_20220140002.ui.view.jadwal

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2_20220140002.data.entity.Jadwal
import com.example.ucp2_20220140002.ui.costumwidget.TopAppBar
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.DetailJadwalViewModel
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.DetailUiState
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.PenyediaJadwalViewModel
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.toJadwalEntity

@Composable
fun DetailJadwalView(
    idPasien: String,
    modifier: Modifier = Modifier,
    viewModel: DetailJadwalViewModel = viewModel(factory = PenyediaJadwalViewModel.FactoryJadwal),
    onBack: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val detailJadwalUiState by viewModel.detailUiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                judul = "Detail Jadwal",
                showBackButton = true,
                onBack = onBack,
            )
        },
        modifier = modifier,
        floatingActionButton = {
            if (!detailJadwalUiState.isLoading && detailJadwalUiState.isUiEventNotEmpty) {
                FloatingActionButton(
                    onClick = {
                        detailJadwalUiState.detailUiEvent.idPasien?.let(onEditClick)
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Jadwal"
                    )
                }
            }
        }
    ) { innerPadding ->
        BodyDetailJadwal(
            modifier = Modifier.padding(innerPadding),
            detailJadwalUiState = detailJadwalUiState,
            onDeleteClick = {
                viewModel.deleteJadwal()
                onDeleteClick()
            }
        )
    }
}

@Composable
fun BodyDetailJadwal(
    modifier: Modifier = Modifier,
    detailJadwalUiState: DetailUiState = DetailUiState(),
    onDeleteClick: () -> Unit
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    when {
        detailJadwalUiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        detailJadwalUiState.isUiEventNotEmpty -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ItemDetailJadwal(
                    jadwal = detailJadwalUiState.detailUiEvent.toJadwalEntity(),
                    modifier = Modifier
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        deleteConfirmationRequired = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Delete")
                }
                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick()
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        detailJadwalUiState.isUiEventEmpty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun ItemDetailJadwal(
    modifier: Modifier = Modifier,
    jadwal: Jadwal
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ComponentDetailJadwal(judul = "ID Pasien", isinya = jadwal.idPasien)
            Spacer(modifier = Modifier.height(4.dp))

            ComponentDetailJadwal(judul = "Nama Dokter", isinya = jadwal.namaDokter)
            Spacer(modifier = Modifier.height(4.dp))

            ComponentDetailJadwal(judul = "Nama Pasien", isinya = jadwal.namaPasien)
            Spacer(modifier = Modifier.height(4.dp))

            ComponentDetailJadwal(judul = "No HP Pasien", isinya = jadwal.noHPpasien)
            Spacer(modifier = Modifier.height(4.dp))

            ComponentDetailJadwal(judul = "Tanggal Konsultasi", isinya = jadwal.tanggalKonsultasi)
            Spacer(modifier = Modifier.height(4.dp))

            ComponentDetailJadwal(judul = "Status", isinya = jadwal.Status)
        }
    }
}

@Composable
fun ComponentDetailJadwal(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "$judul:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = isinya,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* do nothing */ },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}
