package com.example.appmodoguardian_grupo1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.appmodoguardian_grupo1.ui.theme.AppModoGuardian_Grupo1Theme
import com.example.appmodoguardian_grupo1.ui.theme.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppModoGuardian_Grupo1Theme {
                HomeScreen()
            }
        }
    }
}