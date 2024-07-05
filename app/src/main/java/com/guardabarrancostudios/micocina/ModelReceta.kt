package com.guardabarrancostudios.micocina

data class ModelReceta(
    val idUsuario: Int, // Nuevo campo para el ID del usuario
    val tituloReceta: String,
    val ingredientes: String,
    val descripcionReceta: String,
    val tiempoPreparacion: String,
    val nombreCategoria: String,
    val tipoDificultad: String,
    val imagenReceta: String // Nuevo campo para la imagen
)


