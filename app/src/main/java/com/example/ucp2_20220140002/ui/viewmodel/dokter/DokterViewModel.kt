package com.example.ucp2_20220140002.ui.viewmodel.dokter

import com.example.ucp2_20220140002.repository.RepositoryRS
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2_20220140002.data.entity.Dokter
import kotlinx.coroutines.launch

class DokterViewModel(
    private val repositoryRS: RepositoryRS
) : ViewModel() {
    var uiState by mutableStateOf(DokterUIState())

    // Memperbarui state berdasarkan input pengguna
    fun updateState(dokterEvent: DokterEvent) {
        uiState = uiState.copy(
            dokterEvent = dokterEvent,
        )
    }

    // Validasi data input pengguna
    fun validateFields(): Boolean {
        val event = uiState.dokterEvent
        val errorState = FormErrorStateDokter(
            id = if (event.id.isNotEmpty()) null else "ID tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            spesialis = if (event.spesialis.isNotEmpty()) null else "Spesialis tidak boleh kosong",
            klinik = if (event.klinik.isNotEmpty()) null else "Klinik tidak boleh kosong",
            noHp = if (event.noHp.isNotEmpty()) null else "NoHP tidak boleh kosong",
            jamKerja = if (event.jamKerja.isNotEmpty()) null else "NoHP tidak boleh kosong"
        )
        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    // Simpan data dokter
    fun saveData() {
        val currentEvent = uiState.dokterEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    // Menyimpan data dokter
                    repositoryRS.insertDokter(currentEvent.toDokterEntity())

                    uiState = uiState.copy(
                        snackBarMessage = "Data Dokter Berhasil Disimpan",
                        dokterEvent = DokterEvent(), // Reset form
                        isEntryValid = FormErrorStateDokter() // Reset error state
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

// Data class untuk menyimpan data input form dokter
data class DokterEvent(
    val id: String = "",
    val nama: String = "",
    val spesialis: String = "",
    val klinik: String = "",
    val noHp: String = "",
    val jamKerja: String = ""
)

fun DokterEvent.toDokterEntity(): Dokter = Dokter(
    id = id,
    nama = nama,
    spesialis = spesialis,
    klinik = klinik,
    noHp = noHp,
    jamKerja = jamKerja
)

// Data class untuk validasi form dokter
data class FormErrorStateDokter(
    val id: String? = null,
    val nama: String? = null,
    val spesialis: String? = null,
    val klinik: String? = null,
    val noHp: String? = null,
    val jamKerja: String? = null,
) {
    fun isValid(): Boolean {
        return id == null
                && nama == null
                && spesialis == null
                && klinik == null
                && noHp == null
                && jamKerja == null
    }
}

// Data class untuk menyimpan UI state dokter
data class DokterUIState(
    val dokterEvent: DokterEvent = DokterEvent(),
    val isEntryValid: FormErrorStateDokter = FormErrorStateDokter(),
    val snackBarMessage: String? = null
)
