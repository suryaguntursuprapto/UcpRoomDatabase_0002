package com.example.ucp2_20220140002.repository

import android.util.Log
import com.example.ucp2_20220140002.data.dao.DokterDAO
import com.example.ucp2_20220140002.data.dao.JadwalDAO
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.data.entity.Jadwal
import kotlinx.coroutines.flow.Flow

class LocalRepositoryRS(
    private val dokterDAO: DokterDAO,
    private val jadwalDAO: JadwalDAO
): RepositoryRS {
    // dari entitas dokter
   override suspend fun insertDokter(dokter: Dokter){
       dokterDAO.insertDokter(dokter)
   }
    override fun getAllDokter(): Flow<List<Dokter>> {
       return dokterDAO.getAllDokter()
    }
    override fun getDokter(id: String): Flow<Dokter> {
        return dokterDAO.getDokter(id = id)
    }

    //dari entitas jadwal
    override suspend fun insertJadwal(jadwal: Jadwal) {
        jadwalDAO.insertJadwal(jadwal)
    }
    override fun getAllJadwal(): Flow<List<Jadwal>> {
        return jadwalDAO.getAllJadwal()
    }
    override fun getJadwal(idPasien: String): Flow<Jadwal?> {
        Log.d("RepositoryRS", "Fetching jadwal with idPasien: $idPasien")
        return jadwalDAO.getJadwal(idPasien)
    }
    override suspend fun deleteJadwal(jadwal: Jadwal) {
        jadwalDAO.deleteJadwal(jadwal)
    }
    override suspend fun updateJadwal(jadwal: Jadwal) {
        jadwalDAO.updateJadwal(jadwal)
    }
}