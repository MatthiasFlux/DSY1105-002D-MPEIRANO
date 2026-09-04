package com.example.miprimerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    DetalleEntradasScreen()
                }
            }
        }
    }
}

val beneficiosExtra = "Acceso a Lounge y bebida gratis"

val listaEntradas: List<Entrada> = listOf(
    EntradaGeneral(id_entrada = 1, precio = 5000.0),
    EntradaGeneral(id_entrada = 2, precio = 5000.0),
    EntradaVip(id_entrada = 3, precio = 12000.0, beneficiosExtra),
    EntradaVip(id_entrada = 4, precio = 12000.0, beneficiosExtra)
)

var ingresoTotal = listaEntradas.sumOf { it.precio }

var totalVip = listaEntradas.count { it is EntradaVip }

@Composable
fun DetalleEntradasScreen() {
    val entradaGeneral = EntradaGeneral(
        id_entrada = 1,
        precio = 5000.0
    )
    val entradaVip = EntradaVip(
        id_entrada = 2,
        precio = 12000.0,
        beneficiosExtra
    )

    Column(modifier = Modifier.padding(24.dp)) {
        Text(text = entradaGeneral.mostrarDetalle())
        Text(text = "\n-------------------\n")
        Text(text = entradaVip.mostrarDetalle())
    }
}