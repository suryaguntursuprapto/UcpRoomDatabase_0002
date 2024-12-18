package com.example.ucp2_20220140002.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ucp2_20220140002.data.dao.DokterDAO
import com.example.ucp2_20220140002.data.dao.JadwalDAO
import com.example.ucp2_20220140002.data.entity.Dokter
import com.example.ucp2_20220140002.data.entity.Jadwal

//mendefinisikan database dengan tabel Dokter dan Jadwal
@Database(entities = [Dokter::class, Jadwal::class] ,version = 1, exportSchema = false)
abstract class RsDatabase : RoomDatabase(){

    abstract fun dokterDAO() : DokterDAO
    abstract fun jadwalDAO() : JadwalDAO

    companion object{
        @Volatile
        private var Instance: RsDatabase? = null

        fun getDatabase(context: Context) : RsDatabase{
            return (Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    RsDatabase::class.java,
                    name = "RsDatabase"
                )
                    .build().also { Instance = it }
            })
        }
    }
}
