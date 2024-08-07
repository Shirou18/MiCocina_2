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

data class ModelRegistrarPerfil(
    val idUsuario: Int,
    val codigoUsuario: String,
    val nombreUsuario: String,
    val correoUsuario: String,
    val contrasenaUsuario: String,
    val descripcionUsuario: String
)

data class ModelUsuarioConReceta(
    val idUsuario: Int,
    val idReceta: Int,
    val tituloReceta: String,
    val imagenReceta: String,
    val descripcionReceta: String
)

data class ModelUsuarioConDetalle(
    val idUsuario: Int,
    val idReceta: Int,
    val tituloReceta: String,
    val nombreCategoria: String,
    val tipoDificultad: String,
    val descripcionReceta: String,
    val tiempoPreparacion: String,
    val ingredientes: String,
    val favorito: Boolean
)
