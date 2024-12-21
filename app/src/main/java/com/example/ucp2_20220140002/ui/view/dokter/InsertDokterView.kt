package com.example.ucp2_20220140002.ui.view.dokter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2_20220140002.ui.costumwidget.TopAppBar
import com.example.ucp2_20220140002.ui.navigation.*
import com.example.ucp2_20220140002.ui.viewmodel.dokter.FormErrorStateDokter
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DokterEvent
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DokterViewModel
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DokterUIState
import com.example.ucp2_20220140002.ui.viewmodel.*
import com.example.ucp2_20220140002.ui.viewmodel.dokter.PenyediaDokterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object DestinasiInsert: AlamatNavigasi {
    override val route: String = "insert_dokter"
}


@Composable
fun InsertDokterView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DokterViewModel = viewModel(factory = PenyediaDokterViewModel.FactoryDokter)
) {
    val uiState = viewModel.uiState // Ambil UI State dari ViewModel
    val snackbarHostState = remember { SnackbarHostState() } //Snackbar State
    val coroutineScope = rememberCoroutineScope()

    // Observasi perubahan snackbarMessage
    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message) // Tampilkan Snackbar
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
                judul = "Tambah Dokter"
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Isi Body
            InsertBodyDokter(
                uiState = uiState,
                onValueChange = { updatedEvent ->
                    viewModel.updateState(updatedEvent) // Update state di view
                },
                onClick = {
                    coroutineScope.launch {
                        if (viewModel.validateFields()) {
                            viewModel.saveData()
                            delay(600)
                            withContext(Dispatchers.Main) {
                                onNavigate()
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun InsertBodyDokter(
    modifier: Modifier = Modifier,
    onValueChange: (DokterEvent) -> Unit,
    uiState: DokterUIState,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormDokter(
            dokterEvent = uiState.dokterEvent,
            onValueChange = onValueChange,
            errorState = uiState.isEntryValid,
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun FormDokter(
    dokterEvent: DokterEvent = DokterEvent(),
    onValueChange: (DokterEvent) -> Unit = {},
    errorState: FormErrorStateDokter = FormErrorStateDokter(),
    modifier: Modifier = Modifier
) {
    // Data dokter spesialis dan warna masing-masing
    val dokterSpesialisList = listOf(
        "Dokter Umum" to Color.Black,
        "Dokter Anak" to Color.Blue,
        "Dokter Gigi" to Color.Red,
        "Dokter Kulit" to Color.Green,
        "Dokter Mata" to Color.Magenta
    )

    var expanded by remember { mutableStateOf(false) }
    var selectedSpesialis by remember { mutableStateOf(dokterEvent.spesialis) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.id,
            onValueChange = {
                onValueChange(dokterEvent.copy(id = it))
            },
            label = { Text("ID Dokter") },
            isError = errorState.id != null,
            placeholder = { Text("Masukkan ID dokter") },
        )
        Text(
            text = errorState.id ?: "",
            color = Color.Red
        )

        // Nama Dokter
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.nama,
            onValueChange = {
                onValueChange(dokterEvent.copy(nama = it))
            },
            label = { Text("Nama Dokter") },
            isError = errorState.nama != null,
            placeholder = { Text("Masukkan nama dokter") },
        )
        Text(
            text = errorState.nama ?: "",
            color = Color.Red
        )

        // Dropdown untuk Spesialis
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = selectedSpesialis,
                onValueChange = {},
                label = { Text("Spesialis") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                dokterSpesialisList.forEach { (spesialis, color) ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = spesialis,
                                color = color
                            )
                        },
                        onClick = {
                            selectedSpesialis = spesialis
                            expanded = false
                            onValueChange(dokterEvent.copy(spesialis = spesialis))
                        }
                    )
                }
            }
        }
        Text(
            text = errorState.spesialis ?: "",
            color = Color.Red
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.klinik,
            onValueChange = {
                onValueChange(dokterEvent.copy(klinik = it))
            },
            label = { Text("Klinik Dokter") },
            isError = errorState.klinik != null,
            placeholder = { Text("Masukkan Klinik dokter") },
        )
        Text(
            text = errorState.klinik ?: "",
            color = Color.Red
        )

        // Nomor HP
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.noHp,
            onValueChange = {
                onValueChange(dokterEvent.copy(noHp = it))
            },
            label = { Text("No HP") },
            isError = errorState.noHp != null,
            placeholder = { Text("Masukkan nomor HP dokter") },
        )
        Text(
            text = errorState.noHp ?: "",
            color = Color.Red
        )

        // Jam Kerja
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = dokterEvent.jamKerja,
            onValueChange = {
                onValueChange(dokterEvent.copy(jamKerja = it))
            },
            label = { Text("Jam Kerja") },
            isError = errorState.jamKerja != null,
            placeholder = { Text("Masukkan jam kerja dokter") },
        )
        Text(
            text = errorState.jamKerja ?: "",
            color = Color.Red
        )
    }
}