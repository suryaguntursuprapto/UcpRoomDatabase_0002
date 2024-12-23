package com.example.ucp2_20220140002.ui.view.dokter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ucp2_20220140002.R
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.ui.viewmodel.dokter.HomeDokterViewModel
import com.example.ucp2_20220140002.ui.viewmodel.dokter.HomeUiState
import com.example.ucp2_20220140002.ui.viewmodel.dokter.PenyediaDokterViewModel

@Composable
fun HomeDokterView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    onAddDokter: () -> Unit = { },
    onReadJadwal: () -> Unit = { },
    modifier: Modifier = Modifier,
    viewModel: HomeDokterViewModel = viewModel(factory = PenyediaDokterViewModel.FactoryDokter)
) {
    val state by viewModel.homeUIState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(bottomEnd = 25.dp, bottomStart = 25.dp))
                .background(Color(0xFF2196F3))
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.size(25.dp))
                    Text(
                        text = "Selamat Datang, ",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Text(
                        text = "Di Rumah Sehat", // Added sample text to avoid blank space
                        color = Color.White,
                        fontSize = 25.sp
                    )
                }
                Box(
                    contentAlignment = Alignment.BottomStart
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.rumah),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(80.dp)
                            .clip(CircleShape)
                    )
                }
            }
        }

        // Spacer to separate header and body
        Spacer(modifier = Modifier.height(16.dp))

        // Body Section
        Column(
            modifier = Modifier
                .fillMaxSize() // Make sure the body takes up remaining space
                .padding(16.dp)
        ) {
            // Search Bar
            TextField(
                value = "",
                onValueChange = { onAddDokter },
                placeholder = { Text("Cari dokter") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Action Buttons
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onAddDokter) {
                    Text("Tambah Dokter")
                }
                Button(onClick = onReadJadwal) {
                    Text("Lihat Jadwal")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dokter List Section
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (state.isError) {
                Text(
                    text = "Error: ${state.errorMessage}",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else if (state.listDokter.isNotEmpty()) {
                state.listDokter.forEach { dokter ->
                    DokterItem(dokter = dokter)
                }
            } else {
                Text(
                    text = "Tidak ada data dokter.",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(modifier = Modifier.height(35.dp))
        }
    }
}

@Composable
fun DokterItem(dokter: Dokter) {
    // Peta warna spesialis
    val spesialisColorMap = mapOf(
        "Dokter Umum" to Color.Black,
        "Dokter Anak" to Color.Blue,
        "Dokter Gigi" to Color.Red,
        "Dokter Kulit" to Color.Green,
        "Dokter Mata" to Color.Magenta
    )
    val spesialisColor = spesialisColorMap[dokter.spesialis] ?: Color.Gray // Default warna

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Avatar
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                // Dokter Info
                Text(
                    text = dokter.nama,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = dokter.spesialis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = spesialisColor // Warna berbeda sesuai spesialis
                )
                Text(
                    text = dokter.klinik,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = dokter.noHp,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = dokter.jamKerja,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
