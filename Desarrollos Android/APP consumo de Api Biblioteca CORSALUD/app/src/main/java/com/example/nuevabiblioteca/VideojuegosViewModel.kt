package com.example.nuevabiblioteca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.*

class LibrosViewModel : ViewModel() {
    private val _libros = MutableStateFlow<List<LibroModel>>(emptyList())
    val libros = _libros.asStateFlow()

    private val searchQueryFlow = MutableSharedFlow<String>()

    init {
        searchQueryFlow
            .debounce(300)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isNotEmpty()) {
                    obtenerLibros(query)
                } else {
                    flow { emit(emptyList()) } // Emite una lista vacía cuando la consulta está en blanco
                }
            }
            .onEach { libros ->
                _libros.value = libros
            }
            .launchIn(viewModelScope)
    }

    fun searchLibros(query: String) {
        viewModelScope.launch {
            searchQueryFlow.emit(query)
        }
    }

    private suspend fun obtenerLibros(palabra: String): Flow<List<LibroModel>> = flow {
        val url = construirURL(palabra)
        try {
            val response = RetrofitClient.retrofit.obtenerLibros(url)
            if (response.isSuccessful) {
                val libros = response.body()
                if (libros != null) {
                    emit(libros)
                } else {
                    // Manejo de error: respuesta nula o incorrecta de la API
                    // Puedes decidir cómo manejar esto según tu caso
                }
            } else {
                // Manejo de error: respuesta no exitosa de la API
                // Puedes decidir cómo manejar esto según tu caso
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Manejar excepciones, por ejemplo, problemas de red
        }
    }

    private fun construirURL(palabra: String): String {
        return "${Constantes.BASE_URL}consultapalabra?catalogo=1&tipo=F&palabra=$palabra"
    }
}
