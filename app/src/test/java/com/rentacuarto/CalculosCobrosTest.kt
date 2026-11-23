package com.rentacuarto

import com.rentacuarto.data.repository.CalculosCobros
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class CalculosCobrosTest {
    @Test fun dosAbonosCierranRentaConCargo() {
        val rentas=listOf(CalculosCobros.RentaPendiente(1,53000L,"2026-11-10"))
        val abonos=listOf(CalculosCobros.AbonoDisponible(1,20000L),CalculosCobros.AbonoDisponible(2,33000L))
        val asignadas=CalculosCobros.distribuir(rentas,abonos)
        assertEquals(53000L,asignadas.sumOf { it.montoCent })
        assertEquals("PAGADA", CalculosCobros.estadoRenta(53000,53000,"2026-11-10",LocalDate.parse("2026-11-20")))
    }
    @Test fun distribuyePrimeroALaRentaMasAntigua() {
        val rentas=listOf(CalculosCobros.RentaPendiente(1,50000L,"2026-09-10"),
            CalculosCobros.RentaPendiente(2,60000L,"2026-10-10"))
        val salida=CalculosCobros.distribuir(rentas,listOf(CalculosCobros.AbonoDisponible(7,70000L)))
        assertEquals(listOf(1,2),salida.map { it.rentaId })
        assertEquals(listOf(50000L,20000L),salida.map { it.montoCent })
    }
    @Test fun noCreaAplicacionFicticiaConExcedente() {
        val salida=CalculosCobros.distribuir(
            listOf(CalculosCobros.RentaPendiente(4,50000L,"2026-11-10")),
            listOf(CalculosCobros.AbonoDisponible(2,60000L)))
        assertEquals(50000L,salida.sumOf { it.montoCent })
        assertEquals(-10000L,CalculosCobros.saldoContrato(50000,60000))
    }
    @Test fun estadoVencidoSoloDespuesDeFecha() {
        assertEquals("PENDIENTE", CalculosCobros.estadoRenta(50000,10000,"2026-11-10",LocalDate.parse("2026-11-10")))
        assertEquals("VENCIDA", CalculosCobros.estadoRenta(50000,10000,"2026-11-10",LocalDate.parse("2026-11-11")))
    }
    @Test fun resultadoNoSumaGarantiaInterna() {
        // El filtrado de origen GARANTIA se realiza en AbonoDao; aquí se valida la fórmula.
        assertEquals(73000L,CalculosCobros.resultadoOperativo(100000L,27000L))
    }
    @Test(expected=IllegalArgumentException::class)
    fun rechazaAplicacionDeImportesNegativos() {
        CalculosCobros.distribuir(listOf(CalculosCobros.RentaPendiente(1,-1,"2026-11-10")),emptyList())
    }
}
