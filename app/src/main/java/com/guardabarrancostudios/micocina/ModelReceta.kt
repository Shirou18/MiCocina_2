package com.guardabarrancostudios.micocina

data class ModelReceta(
    val tituloReceta: String,
    val ingredientes: String,
    val descripcionReceta: String,
    val imagenReceta: String,
    val tiempoPreparacion: String,
    val nombreCategoria: String,
    val tipoDificultad: String
)
