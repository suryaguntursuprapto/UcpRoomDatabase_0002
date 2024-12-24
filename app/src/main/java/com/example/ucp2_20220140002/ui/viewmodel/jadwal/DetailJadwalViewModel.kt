package com.example.ucp2_20220140002.ui.viewmodel.jadwal

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2_20220140002.data.entity.Jadwal
import com.example.ucp2_20220140002.repository.RepositoryRS
import com.example.ucp2_20220140002.ui.navigation.DestinasiDetail
import com.example.ucp2_20220140002.ui.navigation.DestinasiDetailJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailJadwalViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryRS: RepositoryRS,
) : ViewModel() {
    private val _idPasien: String = checkNotNull(savedStateHandle[DestinasiDetailJadwal.IDPASIEN])


    val detailUiState: StateFlow<DetailUiState> = repositoryRS.getJadwal(_idPasien)
        .map { jadwal ->
            Log.d("DetailJadwalViewModel", "Jadwal fetched: $jadwal")
            if (jadwal != null) {
                DetailUiState(
                    detailUiEvent = jadwal.toDetailUiEvent(),
                    isLoading = false
                )
            } else {
                DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = "Data tidak ditemukan"
                )
            }
        }
        .onStart {
            emit(DetailUiState(isLoading = true))
        }
        .catch { error ->
            Log.e("DetailJadwalViewModel", "Error fetching jadwal: ${error.message}")
            emit(
                DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = error.message ?: "Error occurred"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,                     // Coroutine scope untuk menyimpan state
            started = SharingStarted.WhileSubscribed(5000), // Berhenti jika tidak ada subscriber dalam 5 detik
            initialValue = DetailUiState(isLoading = true)  // Nilai awal
        )




    fun deleteJadwal() {
        detailUiState.value.detailUiEvent.toJadwalEntity().let {
            viewModelScope.launch {
                repositoryRS.deleteJadwal(it)
            }
        }
    }
}

data class DetailUiState(
    val detailUiEvent: JadwalEvent = JadwalEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == JadwalEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != JadwalEvent()
}

/*
* Data class untuk menampung data yang akan ditampilkan di UI*/

// Memindahkan data dari entity ke UI
fun Jadwal.toDetailUiEvent(): JadwalEvent {
    return JadwalEvent(
        idPasien = idPasien,
        idDokter = idDokter,
        namaDokter = namaDokter,
        namaPasien = namaPasien,
        noHPpasien = noHPpasien,
        tanggalKonsultasi = tanggalKonsultasi,
        status = Status
    )
}
