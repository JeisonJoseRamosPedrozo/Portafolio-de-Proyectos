package com.example.apivideojuegos.modelos

import com.google.gson.annotations.SerializedName

data class VideoJuegoModel(
    @SerializedName("count")
    val total: Int,
    @SerializedName("results")
    val listaVideojuegos: List<VideoJuegosLista>
)

data class VideoJuegosLista(
    @SerializedName("id")
    val id: Int,
    @SerializedName("background_image")
    val imagen: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("metacritic")
    val descripcion: String,
)
