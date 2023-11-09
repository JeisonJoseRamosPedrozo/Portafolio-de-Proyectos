package com.example.nuevabiblioteca

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavManager(viewModel: LibrosViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "inicio"
    ){
        composable("inicio"){
            InicioView(navController, viewModel)
        }
    }
}



