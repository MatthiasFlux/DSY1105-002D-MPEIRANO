package com.example.appmodoguardian_grupo1.model

enum class Rol { ADMIN, SUPERVISOR, OPERADOR }

data class Usuario(
    val email: String,
    val clave: String,
    val rol: Rol
)