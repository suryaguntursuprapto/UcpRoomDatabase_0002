package com.example.ucp2_20220140002.ui.viewmodel.dokter

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2_20220140002.RsApp
import com.example.ucp2_20220140002.ui.view.dokter.*

object PenyediaDokterViewModel{
    val Factory = viewModelFactory {
        initializer {
            HomeDokterViewModel(
                RsApp().containerApp.repositoryRS
            )
        }
        initializer {
            DetailDokterViewModel(
                createSavedStateHandle(),
                RsApp().containerApp.repositoryRS,
            )
        }
    }
}

fun CreationExtras.RsApp():RsApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RsApp)