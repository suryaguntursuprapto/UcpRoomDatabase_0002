package com.example.ucp2_20220140002.ui.navigation

import com.example.ucp2_20220140002.ui.navigation.DestinasiDetail.ID

interface AlamatNavigasi {
    val route: String
}

object DestinasiHome : AlamatNavigasi {
    override val route = "home"
}

object DestinasiHomeJadwal : AlamatNavigasi {
    override val route = "home jadwal"
}

object DestinasiDetail : AlamatNavigasi {
    override val route = "detail"
    const val ID = "id"
    val routesWithArg = "$route/{$ID}"
}

object DestinasiDetailJadwal {
    const val route = "detail_jadwal"
    const val IDPASIEN = "idPasien"
    val routesWithArg = "$route/{$IDPASIEN}"
}



object DestinasiUpdate : AlamatNavigasi {
    override val route = "update"
    const val ID = "id"
    val routesWithArg = "$route/{$ID}"
}