package com.guardabarrancostudios.micocina

data class ModelUsuario(
    val idUsuario: Int,
    val nombreUsuario: String,
    val contraseñaUsuario: String
)

lateinit var listaUsuarios:List<ModelUsuario>