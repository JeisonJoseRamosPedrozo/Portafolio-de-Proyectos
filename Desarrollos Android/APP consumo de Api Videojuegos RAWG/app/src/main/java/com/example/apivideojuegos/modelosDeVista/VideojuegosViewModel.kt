package com.example.apivideojuegos.modelosDeVista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apivideojuegos.modelos.VideoJuegosLista
import com.example.apivideojuegos.peticiones.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideojuegosViewModel : ViewModel() {

    private var allGames: List<VideoJuegosLista> = emptyList()
    private val _juegos = MutableStateFlow<List<VideoJuegosLista>>(emptyList())
    val juegos = _juegos.asStateFlow()

    init {
        obtenerJuegos()
    }

    fun searchGames(query: String) {
        if (query.isNotEmpty()) {
            val filteredGames = allGames.filter { juego ->
                juego.name.contains(query, ignoreCase = true)
            }
            _juegos.value = filteredGames
        } else {
            _juegos.value = allGames
        }
    }

    private fun obtenerJuegos() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val response = RetrofitClient.retrofit.obtenerJuegos()
                val gamesList = response.body()?.listaVideojuegos ?: emptyList()
                allGames = gamesList // Mantén una copia de la lista completa
                _juegos.value = gamesList
            }
        }
    }
}

