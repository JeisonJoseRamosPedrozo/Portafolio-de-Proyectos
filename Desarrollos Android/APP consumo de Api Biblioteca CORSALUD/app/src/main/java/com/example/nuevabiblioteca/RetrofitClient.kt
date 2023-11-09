package com.example.nuevabiblioteca

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient { // Cambia el nombre del objeto

    val retrofit: APILibros by lazy { // Cambia el tipo de Retrofit a APILibros
        Retrofit
            .Builder()
            .baseUrl(Constantes.BASE_URL) // Utiliza la URL base adecuada para la API de biblioteca
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APILibros::class.java) // Utiliza la interfaz APILibros
    }
}
