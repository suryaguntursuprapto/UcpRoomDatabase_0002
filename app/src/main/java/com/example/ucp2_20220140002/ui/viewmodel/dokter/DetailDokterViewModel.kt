package com.example.ucp2_20220140002.ui.viewmodel.dokter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2_20220140002.ui.navigation.DestinasiDetail
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.repository.RepositoryRS
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailDokterViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoryRS: RepositoryRS,
) : ViewModel() {
    private val _id: String = checkNotNull(savedStateHandle[DestinasiDetail.ID])

    val detailUiState: StateFlow<DetailUiState> = repositoryRS.getDokter(_id)
        .filterNotNull()
        .map {
            DetailUiState(
                detailUiEvent = it.toDetailUiEvent(),
                isLoading = false,
            )
        }
        .onStart {
            emit(DetailUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailUiState(
                isLoading = true,
            ),
        )

}

data class DetailUiState(
    val detailUiEvent: DokterEvent = DokterEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == DokterEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != DokterEvent()
}

/*
* Data class untuk menampung data yang akan ditampilkan di UI*/

// Memindahkan data dari entity ke UI
fun Dokter.toDetailUiEvent(): DokterEvent {
    return DokterEvent(
        id = id,
        nama = nama,
        spesialis = spesialis,
        klinik = klinik,
        noHp = noHp,
        jamKerja = jamKerja
    )
}
