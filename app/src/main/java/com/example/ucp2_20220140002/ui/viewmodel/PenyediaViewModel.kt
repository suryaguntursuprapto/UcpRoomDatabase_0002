package com.example.ucp2_20220140002.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2_20220140002.RsApp
import com.example.ucp2_20220140002.ui.view.dokter.*

object PenyediaViewModel{
    val Factory = viewModelFactory {
    }
}

fun CreationExtras.RsApp():RsApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RsApp)