package com.example.miprimerapp

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

open class Entrada(val idEntrada: Int, var precio: Double) {


    open fun mostrarDetalle(): String {
        return "Entrada $idEntrada cuesta: $$precio pesos"
    }
}

class EntradaVip(idEntrada: Int, precio: Double, val beneficiosExtra: String): Entrada(idEntrada,precio) {

    override fun mostrarDetalle(): String {
        var descripcionVip = super.mostrarDetalle()
        descripcionVip += "\nBeneficios extra por entrada VIP: $beneficiosExtra\nDisfrute la función"
        return descripcionVip
    }
}

class EntradaGeneral(idEntrada: Int, precio: Double): Entrada(idEntrada,precio) {
    override fun mostrarDetalle(): String {
        var descripcionGeneral = super.mostrarDetalle()
        descripcionGeneral += "\nDisfrute la función"
        return descripcionGeneral
    }
}

sealed class EstadoValidacion {
    data class Validado(val detalleEntrada: String) : EstadoValidacion()
    data class NoValidado(val mensajeError: String) : EstadoValidacion()
    object Validando : EstadoValidacion()
}
suspend fun validarEntrada(idBuscar: Int, listaBuscar: List<Entrada>): EstadoValidacion {
    delay(2000)
    val entradaBuscar = listaBuscar.find { it.idEntrada == idBuscar }

    return if (entradaBuscar != null){
        EstadoValidacion.Validado(entradaBuscar.mostrarDetalle())
    } else {
        EstadoValidacion.NoValidado("La entrada de id $idBuscar no existe")
    }

}

class EntradasPrueba {
    @Test
    fun main(): Unit = runBlocking {

        val beneficiosExtra = "Acceso a Lounge, Bebida y hamburguesa gratis!"

        val listaEntradas: List<Entrada> = listOf(
            EntradaGeneral(idEntrada = 1, precio = 5000.0),
            EntradaGeneral(idEntrada = 2, precio = 5000.0),
            EntradaVip(idEntrada = 3, precio = 12000.0, beneficiosExtra),
            EntradaVip(idEntrada = 4, precio = 12000.0, beneficiosExtra)
        )

        val ingresoTotal = listaEntradas.sumOf { it.precio }
        println("Ingreso total generado: $$ingresoTotal")

        val cantidadVip = listaEntradas.count { it is EntradaVip }
        println("Cantidad de entradas VIP vendidas: $cantidadVip")

        val resultadoBuscar = validarEntrada(3, listaEntradas)

        when (resultadoBuscar) {
            is EstadoValidacion.Validando -> println("Procesando ...")
            is EstadoValidacion.Validado -> println("---- ENTRADA ENCONTRADA! ----\n${resultadoBuscar.detalleEntrada}")
            is EstadoValidacion.NoValidado -> println("---- ERROR ---- \n${resultadoBuscar.mensajeError}")
        }
    }

}