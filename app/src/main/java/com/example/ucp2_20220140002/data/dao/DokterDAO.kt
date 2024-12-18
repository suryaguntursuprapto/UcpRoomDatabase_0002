package com.example.ucp2_20220140002.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ucp2_20220140002.data.entity.Dokter
import kotlinx.coroutines.flow.Flow

@Dao
interface DokterDAO {
    @Insert
    suspend fun insertDokter(dokter: Dokter)
    @Query("SELECT * FROM dokter ORDER BY nama ASC")
    suspend fun getAllDokter():Flow<List<Dokter>>
    @Query("SELECT * FROM dokter WHERE id = :id ")
    suspend fun getDokter(id: String): Flow<Dokter>
}