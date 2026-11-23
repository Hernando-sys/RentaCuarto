package com.rentacuarto.data.repository

import java.time.LocalDate

/** Lógica pura: se prueba sin emulador, Android ni base de datos. */
object CalculosCobros {
    data class RentaPendiente(val rentaId: Int, val exigibleCent: Long, val vencimiento: String)
    data class AbonoDisponible(val abonoId: Int, val montoCent: Long)
    data class Aplicacion(val abonoId: Int, val rentaId: Int, val montoCent: Long)

    /** Los DAO entregan rentas y abonos ya ordenados (antigüedad). */
    fun distribuir(rentas: List<RentaPendiente>, abonos: List<AbonoDisponible>): List<Aplicacion> {
        require(rentas.all { it.exigibleCent >= 0L }) { "Hay rentas con importes inválidos." }
        require(abonos.all { it.montoCent > 0L }) { "Hay abonos con importes inválidos." }
        val restantes = rentas.map { it.exigibleCent }.toMutableList()
        val salida = mutableListOf<Aplicacion>()
        for (abono in abonos) {
            var sinAplicar = abono.montoCent
            for (i in rentas.indices) {
                if (sinAplicar == 0L) break
                val aplicada = minOf(restantes[i], sinAplicar)
                if (aplicada > 0L) {
                    salida += Aplicacion(abono.abonoId, rentas[i].rentaId, aplicada)
                    restantes[i] -= aplicada
                    sinAplicar -= aplicada
                }
            }
            // Excedente queda como saldo a favor; NO se crea aplicación ficticia.
        }
        return salida
    }

    fun estadoRenta(exigibleCent: Long, aplicadoCent: Long, vencimiento: String,
                    fechaCorte: LocalDate): String {
        require(exigibleCent >= 0L && aplicadoCent >= 0L && aplicadoCent <= exigibleCent)
        return when {
            exigibleCent == aplicadoCent -> "PAGADA"
            LocalDate.parse(vencimiento).isBefore(fechaCorte) -> "VENCIDA"
            else -> "PENDIENTE"
        }
    }

    fun saldoContrato(exigibleCent: Long, abonosImputablesCent: Long): Long =
        Math.subtractExact(exigibleCent, abonosImputablesCent)

    fun resultadoOperativo(efectivoCent: Long, gastosCent: Long): Long =
        Math.subtractExact(efectivoCent, gastosCent)
}
