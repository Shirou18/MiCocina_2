package com.guardabarrancostudios.micocina

data class ModelLogin(
    val idUsuario: Int,
    val nombreUsuario: String,
    val contraseñaUsuario: String
)

lateinit var listaUsuarios:List<ModelLogin>

data class ModelRegistrarUsuario(
    val codigoUsuario: String,
    val nombreUsuario: String,
    val correoUsuario: String,
    val contraseñaUsuario: String,
    val imagenUsuario: String,
    val descripcionUsuario: String
)

/*data class ModelUsuarioConReceta(
    val idUsuario: Int,
    val nombreUsuario: String,
    val imagenUsuario: String,
    val tituloReceta: String,
    val imagenReceta: String
)*/