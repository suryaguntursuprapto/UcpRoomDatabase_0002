package com.example.ucp2_20220140002.ui.view.jadwal

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.ui.costumwidget.TopAppBar
import com.example.ucp2_20220140002.ui.navigation.AlamatNavigasi
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.FormErrorStateJadwal
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.JadwalEvent
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.JadwalUIState
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.JadwalViewModel
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.PenyediaJadwalViewModel
import kotlinx.coroutines.launch

object DestinasiInsertJadwal : AlamatNavigasi {
    override val route: String = "insert_jadwal"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormJadwal(
    jadwalEvent: JadwalEvent,
    onValueChange: (JadwalEvent) -> Unit,
    errorState: FormErrorStateJadwal,
    dokterList: List<Dokter>, // Tambahkan daftar dokter
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDokter by remember { mutableStateOf(jadwalEvent.idDokter to jadwalEvent.namaDokter) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.idPasien,
            onValueChange = { onValueChange(jadwalEvent.copy(idPasien = it)) },
            label = { Text("id Pasien") },
            isError = errorState.idPasien != null,
            placeholder = { Text("Masukkan Nama Pasien") }
        )
        Text(text = errorState.idPasien ?: "", color = Color.Red)
        // Dropdown untuk memilih Dokter
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = selectedDokter.second, // Nama dokter yang terpilih
                onValueChange = {},
                label = { Text("Pilih Dokter") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dokterList.forEach { dokter ->
                    DropdownMenuItem(
                        text = { Text("${dokter.id} - ${dokter.nama}") },
                        onClick = {
                            selectedDokter = dokter.id to dokter.nama
                            expanded = false
                            onValueChange(
                                jadwalEvent.copy(
                                    idDokter = dokter.id,
                                    namaDokter = dokter.nama
                                )
                            )
                        }
                    )
                }
            }
        }
        Text(
            text = errorState.idDokter ?: "",
            color = Color.Red
        )

        // Kolom lain tetap sama
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.namaPasien,
            onValueChange = { onValueChange(jadwalEvent.copy(namaPasien = it)) },
            label = { Text("Nama Pasien") },
            isError = errorState.namaPasien != null,
            placeholder = { Text("Masukkan Nama Pasien") }
        )
        Text(text = errorState.namaPasien ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.noHPpasien,
            onValueChange = { onValueChange(jadwalEvent.copy(noHPpasien = it)) },
            label = { Text("No HP Pasien") },
            isError = errorState.noHPpasien != null,
            placeholder = { Text("Masukkan No HP Pasien") }
        )
        Text(text = errorState.noHPpasien ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.tanggalKonsultasi,
            onValueChange = { onValueChange(jadwalEvent.copy(tanggalKonsultasi = it)) },
            label = { Text("Tanggal Konsultasi") },
            isError = errorState.tanggalKonsultasi != null,
            placeholder = { Text("Masukkan Tanggal Konsultasi") }
        )
        Text(text = errorState.tanggalKonsultasi ?: "", color = Color.Red)

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = jadwalEvent.status,
            onValueChange = { onValueChange(jadwalEvent.copy(status = it)) },
            label = { Text("Status") },
            isError = errorState.status != null,
            placeholder = { Text("Masukkan Status") }
        )
        Text(text = errorState.status ?: "", color = Color.Red)
    }
}

@Composable
fun InsertBodyJadwal(
    uiState: JadwalUIState,
    onValueChange: (JadwalEvent) -> Unit,
    onClick: () -> Unit,
    dokterList: List<Dokter>, // Tambahkan daftar dokter
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormJadwal(
            jadwalEvent = uiState.jadwalEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
            dokterList = dokterList,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                // Tambahkan log untuk memastikan fungsi dipanggil
                println("Data Berhasil Disimpan")
                onClick()  // Panggil fungsi onClick yang diberikan
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = true  // Pastikan tombol diaktifkan
        ) {
            Text("Simpan")
        }
    }
}

@Composable
fun InsertJadwalView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JadwalViewModel = viewModel(factory = PenyediaJadwalViewModel.FactoryJadwal)
) {
    val uiState = viewModel.uiState
    val dokterList by viewModel.dokterList.collectAsState(emptyList()) // Observasi data dokter
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                onBack = onBack,
                showBackButton = true,
                judul = "Tambah Jadwal"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            InsertBodyJadwal(
                uiState = uiState,
                onValueChange = { event -> viewModel.updateState(event) },
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.validateFields()) {
                            viewModel.saveData()
                            onNavigate()
                        }
                    }
                },
                dokterList = dokterList // Berikan daftar dokter
            )
        }
    }
}
