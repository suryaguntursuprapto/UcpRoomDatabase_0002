package com.example.ucp2_20220140002.repository

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.data.entity.Jadwal
import kotlinx.coroutines.flow.Flow

interface RepositoryRS {
    //tabel atau entitas dokter
    suspend fun insertDokter(dokter: Dokter)
    fun getAllDokter(): Flow<List<Dokter>>
    fun getDokter(id: String): Flow<Dokter>

    //dari entitas jadwal
    suspend fun insertJadwal(jadwal: Jadwal)
    fun getAllJadwal(): Flow<List<Jadwal>>
    fun getJadwal(idPasien: String): Flow<Jadwal>
    suspend fun deleteJadwal(jadwal: Jadwal)
    suspend fun updateJadwal(jadwal: Jadwal)
}