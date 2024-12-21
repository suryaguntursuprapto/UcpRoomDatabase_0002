package com.example.ucp2_20220140002.ui.viewmodel.jadwal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2_20220140002.data.entity.Jadwal
import com.example.ucp2_20220140002.repository.RepositoryRS
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeJadwalViewModel (
    private val repositoryRS: RepositoryRS
) : ViewModel() {

    val homeJdwlUIState: StateFlow<HomeUiState> = repositoryRS.getAllJadwal()
        .filterNotNull()
        .map {
            HomeUiState (
                listJadwal = it.toList(),
                isLoading = false,
            )
        }
        .onStart {
            emit(HomeUiState(isLoading = true))
            delay(900)
        }
        .catch {
            emit(
                HomeUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState (
                isLoading = true,
            )
        )
}

data class HomeUiState (
    val listJadwal: List<Jadwal> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)