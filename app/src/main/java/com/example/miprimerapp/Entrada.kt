package com.example.miprimerapp

open class Entrada(val id_entrada: Int, var precio: Double) {


    open fun mostrarDetalle(): String {
        return "Entrada $id_entrada cuesta: $$precio pesos"
    }
}

class EntradaVip(id_entrada: Int, precio: Double, val beneficiosExtra: String): Entrada(id_entrada,precio) {

    override fun mostrarDetalle(): String {
        var descripcionVip = super.mostrarDetalle()
        descripcionVip += "\nBeneficios extra por entrada VIP: $beneficiosExtra\nDisfrute la función"
        return descripcionVip
    }
}

class EntradaGeneral(id_entrada: Int, precio: Double): Entrada(id_entrada,precio) {
    override fun mostrarDetalle(): String {
        var descripcionGeneral = super.mostrarDetalle()
        descripcionGeneral += "\nDisfrute la función"
        return descripcionGeneral
    }
}