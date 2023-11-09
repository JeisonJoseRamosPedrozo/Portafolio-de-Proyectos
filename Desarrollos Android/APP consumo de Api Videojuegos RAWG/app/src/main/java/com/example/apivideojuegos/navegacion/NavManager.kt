package com.example.apivideojuegos.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apivideojuegos.modelosDeVista.VideojuegosViewModel
import com.example.apivideojuegos.vistas.InicioView

@Composable
fun NavManager(viewModel: VideojuegosViewModel) {
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