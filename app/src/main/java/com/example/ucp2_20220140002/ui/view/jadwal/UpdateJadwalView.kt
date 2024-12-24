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
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.FormErrorStateJadwal
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.JadwalEvent
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.JadwalUIState
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.JadwalViewModel
import com.example.ucp2_20220140002.ui.viewmodel.jadwal.PenyediaJadwalViewModel
import kotlinx.coroutines.launch

@Composable
fun UpdateJadwalView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JadwalViewModel = viewModel(factory = PenyediaJadwalViewModel.FactoryJadwal)
) {
    val uiState = viewModel.uiState
    val dokterList by viewModel.dokterList.collectAsState(emptyList())
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
                judul = "Edit Jadwal"
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
                            viewModel.updateData() // Simpan perubahan ke repository
                            onNavigate() // Navigasi setelah berhasil
                        }
                    }
                },
                dokterList = dokterList
            )
        }
    }
}
