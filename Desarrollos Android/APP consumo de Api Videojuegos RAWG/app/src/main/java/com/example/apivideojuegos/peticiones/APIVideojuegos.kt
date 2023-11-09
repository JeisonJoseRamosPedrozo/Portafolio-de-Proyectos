package com.example.apivideojuegos.peticiones

import com.example.apivideojuegos.modelos.VideoJuegoModel
import com.example.apivideojuegos.utilidades.Constantes
import retrofit2.Response
import retrofit2.http.GET

interface APIVideojuegos {

    @GET("games${Constantes.API_KEY}")
    suspend fun obtenerJuegos(): Response<VideoJuegoModel>
}