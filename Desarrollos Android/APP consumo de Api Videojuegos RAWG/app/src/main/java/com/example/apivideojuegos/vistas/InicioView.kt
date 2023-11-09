package com.example.apivideojuegos.vistas

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.apivideojuegos.componentes.CardJuego
import com.example.apivideojuegos.componentes.MainTopBar
import com.example.apivideojuegos.modelosDeVista.VideojuegosViewModel
import com.example.apivideojuegos.utilidades.Constantes
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun InicioView(
    navController: NavController,
    viewModel: VideojuegosViewModel
) {
    val scope = rememberCoroutineScope()
    val juegos by viewModel.juegos.collectAsState()
    var searchTerm by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            MainTopBar(titulo = "RAWG, Selección Mejores Videojuegos Del Momento")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(Constantes.COLOR_ROJO))

        ) {
            BasicTextField(
                value = searchTerm,
                onValueChange = {
                    searchTerm = it
                    scope.launch {
                        viewModel.searchGames(it)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White) // Fondo blanco para el buscador
            )

            LazyColumn {
                items(juegos) { juego ->
                    CardJuego(juego = juego) {
                        // Cuando se haga clic en el Card, se mostrará la descripción
                    }
                }
            }
        }
    }
}


