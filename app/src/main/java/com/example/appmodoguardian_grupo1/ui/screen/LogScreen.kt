package com.example.appmodoguardian_grupo1.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appmodoguardian_grupo1.R

// Estructura del Home page
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Modo Guardian") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "¡Bienvenido!")
            Button(
                onClick = { navController.navigate("login") }
            ) {
                Text("Iniciar Sesión")
            }
            Button(onClick = { navController.navigate("registro") }) {
                Text("Ir a Registro")
            }
            // Insercion de imagen logo desde carpeta res en el projecto
            Image(
                painter = painterResource(id = R.drawable.logo_generico),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

