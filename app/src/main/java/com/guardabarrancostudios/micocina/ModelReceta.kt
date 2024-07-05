package com.guardabarrancostudios.micocina

data class ModelReceta(
    val tituloReceta: String,
    val ingredientes: String,
    val descripcionReceta: String,
    val tiempoPreparacion: String,
    val nombreCategoria: String,
    val tipoDificultad: String,
    val imagenReceta: String // Nuevo campo para la imagen
)

