package com.example.ucp2_20220140002.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dokter")
data class Dokter(
    @PrimaryKey
    val id: String,
    val nama: String,
    val spesialis: String,
    val klinik: String,
    val noHp: String,
    val jamKerja: String
)
