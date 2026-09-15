package org.example

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// Clase padre para los usuarios
open class TipoUsuario(val nombre: String) {
    // Metodos abiertos para aplicar plimorfismmo
    open fun ajustarMinutos(minutos: Int): Double = minutos.toDouble()
    open fun aplicarDescuentoEmpresa(montoConIva: Double): Double = montoConIva
}

// Clases hijas de TipoUsu  ario
class UsuarioRegular : TipoUsuario("Regular")

class UsuarioSuscriptor : TipoUsuario("Suscriptor") {
    // Aplicacion de regla de negocio de 20% descuento para usuario Suscriptor
    override fun ajustarMinutos(minutos: Int): Double = minutos * 0.80
}

class UsuarioEmpresa : TipoUsuario("Empresa") {
    // Aplicación de regla de negocio de 50% descuento para usuario Empresa
    override fun aplicarDescuentoEmpresa(montoConIva: Double): Double = montoConIva * 0.50
}

// Clase padre Maquina
open class Maquina( val codigo: String, val marcaModelo: String, val tipoUsuario: TipoUsuario) {

    // Validación de codigo valido segun requerimiento
    open fun esCodigoValido(codigo: String): Boolean {
        // Debe tener 6 caracteres
        if (codigo.length != 6) return false

        // Verifica 2 Letras - 2 Números - 2 Letras, en ese orden
        return codigo[0].isLetter() && codigo[1].isLetter() &&
                codigo[2].isDigit()  && codigo[3].isDigit()  &&
                codigo[4].isLetter() && codigo[5].isLetter()
    }
    open fun calcularTarifaBase(minutos: Int): Double = 0.0

    open fun mostrarDetalle(): String {
        return "Máquina $codigo ($marcaModelo) - Usuario: ${tipoUsuario.nombre}"
    }
}

// Polimorfismo de Maquina
class Lavadora(codigo: String, marcaModelo: String, tipoUsuario: TipoUsuario ) :
    Maquina(codigo, marcaModelo, tipoUsuario) {

    // Sobrescritura aplicada a los metodos para Lavadora
    override fun calcularTarifaBase(minutos: Int): Double {
        val minutosAjustados = tipoUsuario.ajustarMinutos(minutos)
        return (minutosAjustados / 60.0) * 1200.0
    }

    override fun mostrarDetalle(): String {
        return "${super.mostrarDetalle()}\nTipo: Lavadora | Tarifa base: $1200/hr"
    }
}

class Secadora(codigo: String, marcaModelo: String, tipoUsuario: TipoUsuario) :
    Maquina(codigo, marcaModelo, tipoUsuario) {

    // Sobrescritura aplicada a los metodos para Secadora
    override fun calcularTarifaBase(minutos: Int): Double {
        if (minutos < 30) return 0.0
        return (minutos / 60.0) * 1000.0
    }

    override fun mostrarDetalle(): String {
        return "${super.mostrarDetalle()}\nTipo: Secadora | Tarifa base: $1000/hr"
    }
}


class LavasecaIndustrial(codigo: String, marcaModelo: String, tipoUsuario: TipoUsuario, val conVapor: Boolean) :
    Maquina(codigo, marcaModelo, tipoUsuario) {

    // Sobrescritura aplicada a los metodos para LavasecaIndustrial
    override fun calcularTarifaBase(minutos: Int): Double {
        val base = (minutos / 60.0) * 2800.0
        return if (conVapor) base * 1.30 else base
    }

    override fun mostrarDetalle(): String {
        val detalleVapor = if (conVapor) "Con vapor (+30%)" else "Sin vapor"
        return "${super.mostrarDetalle()}\nTipo: Lavaseca Industrial ($detalleVapor) | Tarifa base: $2800/hr"
    }
}


sealed class EstadoSlot {
    object Libre : EstadoSlot()
    data class EnUso(val maquina: Maquina) : EstadoSlot()
    data class EnCicloFinal(val motivo: String) : EstadoSlot()
    data class FueraDeServicio(val motivo: String) : EstadoSlot()
}

class Slot(val numero: Int) {
    var estado: EstadoSlot = EstadoSlot.Libre
}

data class Ticket(val numeroTicket: Int, val maquina: Maquina, val minutosUso: Int, val montoPagado: Double)

sealed class EstadoOperacion {
    object Procesando : EstadoOperacion()
    data class Exito(val mensaje: String, val ticket: Ticket? = null) : EstadoOperacion()
    data class Error(val mensajeError: String) : EstadoOperacion()
}

// Metodos asincronos
suspend fun registrarEntrada(maquina: Maquina, slots: Array<Slot>): EstadoOperacion {
    val slotDisponible = slots.find { it.estado is EstadoSlot.Libre }
        ?: return EstadoOperacion.Error("Sistema sin capacidad: No hay slots libres.")

    slotDisponible.estado = EstadoSlot.EnCicloFinal("Registrando entrada")
    delay(3000) // Simular sensor (3 seg)

    slotDisponible.estado = EstadoSlot.EnUso(maquina)
    return EstadoOperacion.Exito("Maquina ${maquina.codigo} asignada al Slot ${slotDisponible.numero}")
}

suspend fun registrarSalida(
    codigoBuscar: String,
    minutosUso: Int,
    slots: Array<Slot>,
    historial: MutableList<Ticket>,
    numeroTicket: Int
): EstadoOperacion {
    val slotOcupado = slots.find {
        it.estado is EstadoSlot.EnUso && (it.estado as EstadoSlot.EnUso).maquina.codigo == codigoBuscar
    } ?: return EstadoOperacion.Error("Maquina con codigo $codigoBuscar no encontrada en el sistema.")

    val maquina = (slotOcupado.estado as EstadoSlot.EnUso).maquina
    slotOcupado.estado = EstadoSlot.EnCicloFinal("Calculando tarifa")
    delay(6500) // Simula sensor (6.5 seg)

    // Calculo de tarifa (+19% IVA, Descuento a Empresa)
    val tarifaBase = maquina.calcularTarifaBase(minutosUso)
    val tarifaConIva = tarifaBase * 1.19
    val totalPagar = maquina.tipoUsuario.aplicarDescuentoEmpresa(tarifaConIva)

    if (totalPagar <= 0 && !(maquina is Secadora && minutosUso < 30)) {
        slotOcupado.estado = EstadoSlot.EnUso(maquina)
        return EstadoOperacion.Error("Resultado de tarifa inválido ($$totalPagar).")
    }

    val nuevoTicket = Ticket(numeroTicket, maquina, minutosUso, totalPagar)
    historial.add(nuevoTicket)
    slotOcupado.estado = EstadoSlot.Libre

    return EstadoOperacion.Exito("Salida procesada para $codigoBuscar", nuevoTicket)
}



fun main(): Unit = runBlocking {
    val slots = Array(10) { Slot(it + 1) }
    val historialTickets = mutableListOf<Ticket>()

    // Instancias de Tipos de Usuario
    val uRegular = UsuarioRegular()
    val uSuscriptor = UsuarioSuscriptor()
    val uEmpresa = UsuarioEmpresa()

    println("--- PRUEBA DE ERRORES DE CODIGO (EXCEPCIONES) ---")
    try {
        val maquinaInvalida = Lavadora("123ABC", "Samsung WW90", uRegular)
    } catch (e: IllegalArgumentException) {
        println("Excepcion capturada con éxito: ${e.message}\n")
    }

    // Datos de prueba sugeridos
    val m1 = Lavadora("LV12CD", "Samsung WW90", uSuscriptor)
    val m2 = Lavadora("LV99ZA", "LG F4WV509", uRegular)
    val m3 = Secadora("SC22TO", "Bosch WTH85200", uRegular)
    val m4 = LavasecaIndustrial("LI44RG", "Miele PW6", uEmpresa, conVapor = true)
    val m5 = LavasecaIndustrial("LI77RG", "Speed Queen SF7", uRegular, conVapor = false)

    println("--- REGISTRO DE ENTRADAS ASINCRONAS ---")
    val maquinasIngresar = listOf(m1, m2, m3, m4, m5)
    for (m in maquinasIngresar) {
        val resEntrada = registrarEntrada(m, slots)
        when (resEntrada) {
            is EstadoOperacion.Procesando -> println("Procesando entrada...")
            is EstadoOperacion.Exito -> println("EXITO: ${resEntrada.mensaje}")
            is EstadoOperacion.Error -> println("ERROR: ${resEntrada.mensajeError}")
        }
    }

    println("\n--- REGISTRO DE SALIDAS ---")
    // Datos de prueba, el ultimo registra un error, no esta en el registro
    val ordenSalidas = listOf(
        Pair("LV12CD", 75),
        Pair("LV99ZA", 180),
        Pair("SC22TO", 25),
        Pair("LI44RG", 120),
        Pair("LI77RG", 45),
        Pair("AB12CD", 50)
    )

    var contadorTicket = 1
    for ((codigo, minutos) in ordenSalidas) {
        val resSalida =
            registrarSalida(codigo, minutos, slots, historialTickets, contadorTicket)
        when (resSalida) {
            is EstadoOperacion.Procesando -> println("Procesando salida...")
            is EstadoOperacion.Exito -> {
                val t = resSalida.ticket!!
                println("---- TICKET DE SALIDA GENERADO (${t.numeroTicket}) ----")
                println(t.maquina.mostrarDetalle())
                println("Tiempo de uso: ${t.minutosUso} min | Total a pagar: $${"%.0f".format(t.montoPagado)}\n")
                contadorTicket++
            }

            is EstadoOperacion.Error -> {
                println("---- ERROR DE PROCESAMIENTO ----")
                println("${resSalida.mensajeError}\n")
            }
        }
    }

    println("--- ANALISIS DE DATOS Y REPORTE DE CIERRE ---")
    val totalRecaudado = historialTickets.sumOf { it.montoPagado }
    println("Ingreso total generado: $${"%.0f".format(totalRecaudado)}")

    val cantidadSuscriptores =
        historialTickets.count { it.maquina.tipoUsuario is UsuarioSuscriptor }
    println("Cantidad de maquinas atendidas de usuarios Suscriptores: $cantidadSuscriptores")

    val ingresoPromedio =
        if (historialTickets.isNotEmpty()) totalRecaudado / historialTickets.size else 0.0
    println("Ingreso promedio por maquina: $${"%.0f".format(ingresoPromedio)}")

    val codigosFinalizados = historialTickets.map { it.maquina.codigo }
    println("Codigos de maquinas que finalizaron turno: $codigosFinalizados")

    val maquinaMaxUso = historialTickets.maxByOrNull { it.minutosUso }
    println("Maquina con mas tiempo de uso: ${maquinaMaxUso?.maquina?.codigo} con ${maquinaMaxUso?.minutosUso} minutos")

    val slotsDisponibles = slots.count { it.estado is EstadoSlot.Libre }
    println("Cantidad de slots disponibles al cierre: $slotsDisponibles")
}
