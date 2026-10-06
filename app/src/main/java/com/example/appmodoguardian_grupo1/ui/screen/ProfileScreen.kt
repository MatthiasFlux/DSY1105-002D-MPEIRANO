package com.example.appmodoguardian_grupo1.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmodoguardian_grupo1.viewmodel.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    usuarioViewModel: UsuarioViewModel
) {
    val estado by usuarioViewModel.estado.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Perfil de Usuario") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Datos Registrados",
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()
                    Text(text = "Nombre: ${estado.nombre.ifBlank { "No registrado" }}")
                    Text(text = "Correo: ${estado.correo.ifBlank { "No registrado" }}")
                    Text(text = "Dirección: ${estado.direccion.ifBlank { "No registrada" }}")
                    Text(text = "Estado de términos: ${if (estado.aceptaTerminos) "Aceptados" else "No aceptados"}")
                }
            }

            Button(onClick = { navController.popBackStack("home", inclusive = false) }) {
                Text("Volver al Inicio")
            }
        }
    }
}