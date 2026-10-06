package com.example.appmodoguardian_grupo1.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmodoguardian_grupo1.ui.screen.LogScreen
import com.example.appmodoguardian_grupo1.ui.screen.LoginScreen
import com.example.appmodoguardian_grupo1.ui.screen.HomeScreen
import com.example.appmodoguardian_grupo1.ui.screen.RegistroScreen
import com.example.appmodoguardian_grupo1.ui.screen.ResumenScreen
import com.example.appmodoguardian_grupo1.ui.screen.ProfileScreen
import com.example.appmodoguardian_grupo1.viewmodel.UsuarioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val usuarioViewModel: UsuarioViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "log" // 1. La app abre en la bienvenida (LogScreen)
    ) {
        // 1. Bienvenida / Selección
        composable("log") {
            LogScreen(navController = navController)
        }

        // 2. Formulario de Autenticación
        composable("login") {
            LoginScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        // 3. Pantalla Principal tras validar credenciales
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = usuarioViewModel
            )
        }

        // Otras pantallas secundarias
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
        composable("perfil") {
            ProfileScreen(
                navController = navController,
                usuarioViewModel = usuarioViewModel
            )
        }
    }
}