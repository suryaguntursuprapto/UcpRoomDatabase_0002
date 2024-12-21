package com.example.ucp2_20220140002.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "jadwal",
    foreignKeys = [
        ForeignKey(
            entity = Dokter::class,
            parentColumns = ["id"],
            childColumns = ["idDokter"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ])
data class Jadwal(
    @PrimaryKey
    val idPasien: String,
    @ColumnInfo(index = true)
    val idDokter: String,
    val namaDokter: String,
    val namaPasien: String,
    val noHPpasien: String,
    val tanggalKonsultasi: String,
    val Status: String
)
