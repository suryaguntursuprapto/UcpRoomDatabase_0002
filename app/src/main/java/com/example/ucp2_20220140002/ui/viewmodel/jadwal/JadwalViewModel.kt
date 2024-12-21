package com.example.ucp2_20220140002.ui.viewmodel.jadwal

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.data.entity.Jadwal
import com.example.ucp2_20220140002.repository.RepositoryRS
import kotlinx.coroutines.launch

class JadwalViewModel(
    private val repositoryJadwal: RepositoryRS
) : ViewModel() {
    var uiState by mutableStateOf(JadwalUIState())

    // Memperbarui state berdasarkan input pengguna
    fun updateState(jadwalEvent: JadwalEvent) {
        uiState = uiState.copy(
            jadwalEvent = jadwalEvent,
        )
    }

    // Validasi data input pengguna
    private fun validateFields(): Boolean {
        val event = uiState.jadwalEvent
        val errorState = FormErrorStateJadwal(
            idPasien = if (event.idPasien.isNotEmpty()) null else "ID Pasien tidak boleh kosong",
            namaDokter = if (event.namaDokter.isNotEmpty()) null else "Nama Dokter tidak boleh kosong",
            namaPasien = if (event.namaPasien.isNotEmpty()) null else "Nama Pasien tidak boleh kosong",
            noHPpasien = if (event.noHPpasien.isNotEmpty()) null else "No HP Pasien tidak boleh kosong",
            tanggalKonsultasi = if (event.tanggalKonsultasi.isNotEmpty()) null else "Tanggal Konsultasi tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong"
        )
        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    // Simpan data jadwal
    fun saveData() {
        val currentEvent = uiState.jadwalEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryJadwal.insertJadwal(currentEvent.toJadwalEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data Jadwal Berhasil Disimpan",
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormErrorStateJadwal()
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackBarMessage = "Data Gagal Disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackBarMessage = "Input Tidak Valid. Periksa Kembali Data Anda."
            )
        }
    }

    // Reset pesan snackbar
    fun resetSnackBarMessage() {
        uiState = uiState.copy(snackBarMessage = null)
    }
}

// Data class untuk menyimpan data input form jadwal
data class JadwalEvent(
    val idPasien: String = "",
    val idDokter: String = "",
    val namaDokter: String = "",
    val namaPasien: String = "",
    val noHPpasien: String = "",
    val tanggalKonsultasi: String = "",
    val status: String = ""
)

fun JadwalEvent.toJadwalEntity(): Jadwal = Jadwal(
    idPasien = idPasien,
    idDokter = idDokter,
    namaDokter = namaDokter,
    namaPasien = namaPasien,
    noHPpasien = noHPpasien,
    tanggalKonsultasi = tanggalKonsultasi,
    Status = status
)

// Data class untuk validasi form jadwal
data class FormErrorStateJadwal(
    val idPasien: String? = null,
    val idDokter: String? = null,
    val namaDokter: String? = null,
    val namaPasien: String? = null,
    val noHPpasien: String? = null,
    val tanggalKonsultasi: String? = null,
    val status: String? = null
) {
    fun isValid(): Boolean {
        return idPasien == null
                && idDokter == null
                && namaDokter == null
                && namaPasien == null
                && noHPpasien == null
                && tanggalKonsultasi == null
                && status == null
    }
}

// Data class untuk menyimpan UI state jadwal
data class JadwalUIState(
    val jadwalEvent: JadwalEvent = JadwalEvent(),
    val isEntryValid: FormErrorStateJadwal = FormErrorStateJadwal(),
    val snackBarMessage: String? = null
)
