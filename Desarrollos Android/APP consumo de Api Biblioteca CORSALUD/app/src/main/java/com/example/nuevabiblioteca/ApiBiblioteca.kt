package com.example.nuevabiblioteca

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APILibros {
    @GET
    suspend fun obtenerLibros(@Url url: String): Response<List<LibroModel>>
}
