package com.example.ucp2_20220140002.ui.navigation

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ucp2_20220140002.ui.view.dokter.DestinasiInsert
import com.example.ucp2_20220140002.ui.view.dokter.HomeDokterView
import com.example.ucp2_20220140002.ui.view.dokter.InsertDokterView
import com.example.ucp2_20220140002.ui.view.jadwal.DestinasiInsertJadwal
import com.example.ucp2_20220140002.ui.view.jadwal.DetailJadwalView
import com.example.ucp2_20220140002.ui.view.jadwal.HomeJadwalView
import com.example.ucp2_20220140002.ui.view.jadwal.InsertJadwalView
import com.example.ucp2_20220140002.ui.view.jadwal.UpdateJadwalView

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = DestinasiHome.route
    ) {
        // Halaman Home
        composable(route = DestinasiHome.route) {
            // Navigasi ke halaman InsertDokterView
            HomeDokterView(
                onNavigate = {
                    navController.navigate(DestinasiHome.route)
                },
                onBack = {
                    navController.navigateUp()
                },
                onAddDokter = {
                    navController.navigate(DestinasiInsert.route)
                },
                onReadJadwal = {
                    navController.navigate(DestinasiHomeJadwal.route)
                },

                modifier = Modifier
            )
        }
        composable(
            route = DestinasiInsert.route
        ){
            InsertDokterView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        composable(route = DestinasiHomeJadwal.route) {
            // Navigasi ke halaman InsertDokterView
            HomeJadwalView(
                 onNavigateToInsertJadwal =  {
                    navController.navigate(DestinasiInsertJadwal.route)
                },
                onBack = {
                    navController.navigateUp()
                },
                onDetailJadwal = { id ->
                    Log.d("PengelolaHalaman", "Navigating to DetailJadwal with id: $id")
                    navController.navigate("${DestinasiDetailJadwal.route}/$id")
                },
                modifier = Modifier
            )
        }

        composable(
            route = DestinasiInsertJadwal.route
        ){
            InsertJadwalView(
                onBack = {
                    navController.popBackStack()
                },
                onNavigate = {
                    navController.popBackStack()
                },
                modifier = modifier
            )
        }

        composable(
            DestinasiDetailJadwal.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiDetailJadwal.IDPASIEN) {
                    type = NavType.StringType
                }
            )
        ) {
            val idJadwal = it.arguments?.getString(DestinasiDetailJadwal.IDPASIEN)
            idJadwal?.let { id ->
                DetailJadwalView(
                    idPasien = id,
                    onBack = { navController.popBackStack() },
                    onEditClick = { id ->
                        Log.d("PengelolaHalaman", "Navigating to UpdateJadwalView with id: $id")
                        navController.navigate("${DestinasiUpdate.route}/$id")
                    },
                    onDeleteClick = { navController.popBackStack() },
                    modifier = Modifier
                )
            } ?: run {
                // Handle error gracefully jika idJadwal null
                Text("ID Jadwal tidak ditemukan", color = Color.Red)
            }

        }
        composable(
            DestinasiUpdate.routesWithArg,
            arguments = listOf(
                navArgument(DestinasiUpdate.ID) {
                    type = NavType.StringType
                }
            )
        ) {
            val idJadwal = it.arguments?.getString(DestinasiUpdate.ID)
            idJadwal?.let { id ->
                UpdateJadwalView(
                    onBack = { navController.popBackStack() },
                    onNavigate = { navController.popBackStack() },
                    modifier = Modifier
                )
            } ?: run {
                Text("ID Jadwal tidak ditemukan", color = Color.Red)
            }
        }


    }
}
