package com.example.ucp2_20220140002.ui.viewmodel.jadwal

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2_20220140002.RsApp
import com.example.ucp2_20220140002.ui.view.dokter.*
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DetailDokterViewModel
import com.example.ucp2_20220140002.ui.viewmodel.dokter.DokterViewModel
import com.example.ucp2_20220140002.ui.viewmodel.dokter.HomeDokterViewModel

object PenyediaJadwalViewModel{
    val FactoryJadwal = viewModelFactory {
        initializer {
            JadwalViewModel(
                RsApp().containerApp.repositoryRS
            )
        }
        initializer {
            HomeJadwalViewModel(
                RsApp().containerApp.repositoryRS
            )
        }
        initializer {
            DetailJadwalViewModel(
                createSavedStateHandle(),
                RsApp().containerApp.repositoryRS,
            )
        }
        initializer {
            UpdatedJadwalViewModel(
                createSavedStateHandle(),
                RsApp().containerApp.repositoryRS,
            )
        }

    }
}

fun CreationExtras.RsApp():RsApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RsApp)