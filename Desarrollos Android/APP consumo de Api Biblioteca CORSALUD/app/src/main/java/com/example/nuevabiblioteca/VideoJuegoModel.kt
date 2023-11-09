package com.example.nuevabiblioteca


data class LibroModel(
    val woResultadoOpacPK: woResultadoOpacPK,
    val titulo: String,
    val autorTitulo: String,
    val autor: String?,
    val signatura: String,
    val fecha: String?,
    val isbn: String?,
    val edicion: String?,
    val editorial: String,
    val dfisica: String
)

data class woResultadoOpacPK(
    val fkWaEmpresa: Int,
    val ficha: String
)