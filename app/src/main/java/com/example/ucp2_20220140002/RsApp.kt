package com.example.ucp2_20220140002

import android.app.Application
import com.example.ucp2_20220140002.dependeciesinjection.ContainerApp

class RsApp:Application() {
    lateinit var containerApp: ContainerApp //fungsinya untuk menyimpan instance

    override fun onCreate() {
        super.onCreate()
        containerApp = ContainerApp(this) // membuat instance ContainerApp
        //instance adalah object yang dibuat dari class
    }
}