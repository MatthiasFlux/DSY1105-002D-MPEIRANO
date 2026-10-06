package com.example.appmodoguardian_grupo1.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardian_grupo1.ui.theme.HomeScreen // Asegúrate de que coincida con el paquete de tu HomeScreen
import com.example.appmodoguardian_grupo1.ui.screen.RegistroScreen
import com.example.appmodoguardian_grupo1.ui.screen.ResumenScreen
import com.example.appmodoguardian_grupo1.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home" // Ahora la app inicia en la pantalla Home
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("registro") {
            RegistroScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }
        composable("resumen") {
            ResumenScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }
    }
}