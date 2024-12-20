package com.example.ucp2_20220140002.dependeciesinjection

import android.content.Context
import com.example.ucp2_20220140002.data.database.RsDatabase
import com.example.ucp2_20220140002.repository.LocalRepositoryRS
import com.example.ucp2_20220140002.repository.RepositoryRS

interface InterfaceContainerApp {
    val repositoryRS:RepositoryRS
}

class ContainerApp(private val context: Context) : InterfaceContainerApp {
    override val repositoryRS: RepositoryRS by lazy {
        val database = RsDatabase.getDatabase(context)
        LocalRepositoryRS(database.dokterDAO(), database.jadwalDAO())
    }
}