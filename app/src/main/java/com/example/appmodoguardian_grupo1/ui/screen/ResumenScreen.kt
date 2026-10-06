package com.example.appmodoguardian_grupo1.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmodoguardian_grupo1.viewmodel.UsuarioViewModel

@Composable
fun ResumenScreen(
    navController: NavController,
    viewModel: UsuarioViewModel
) {
    val estado by viewModel.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Resumen del Registro",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = "Nombre: ${estado.nombre}")
        Text(text = "Correo: ${estado.correo}")
        Text(text = "Dirección: ${estado.direccion}")
        Text(text = "Contraseña: ${"*".repeat(estado.clave.length)}")
        Text(text = "Términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.popBackStack("home", inclusive = false) }) {
            Text("Volver al Inicio")
        }
    }
}