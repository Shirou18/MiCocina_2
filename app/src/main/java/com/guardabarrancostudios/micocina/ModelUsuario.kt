package com.guardabarrancostudios.micocina

data class ModelUsuario(
    val idUsuario: Int,
    val nombreUsuario: String,
    val contraseñaUsuario: String
)

lateinit var listaUsuarios:List<ModelUsuario>

data class ModeloUsuario(
    val codigoUsuario: String,
    val nombreUsuario: String,
    val correoUsuario: String,
    val contraseñaUsuario: String,
    val imagenUsuario: String,
    val descripcionUsuario: String
)