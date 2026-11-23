package com.rentacuarto.data.repository

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.AbonoEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.YearMonth

/**
 * Consultas derivadas para futuras pantallas de panel e informes.
 * No guarda filas ficticias de saldo / ocupación / estado de cuenta.
 */
class ConsultasRepository(private val bd: RentaCuartoDatabase) {
    data class Ocupacion(val propiedadId:Int,val fecha:String,
                        val total:Int,val ocupadas:Int,val disponibles:Int)
    data class MovimientoCuenta(val fecha:String,val concepto:String,
        val importeCent:Long,val saldoAcumuladoCent:Long,val referenciaId:Int)
    data class DeudaVencida(val contratoId:Int,val saldoVencidoCent:Long)

    fun historialHabitacion(habitacionId: Int) = bd.contratoDao().historialHabitacion(habitacionId)
    fun historialInquilino(inquilinoId: Int) = bd.contratoDao().historialInquilino(inquilinoId)

    suspend fun ocupacion(propiedadId:Int,fechaIso:String): Ocupacion {
        LocalDate.parse(fechaIso)
        require(bd.propiedadDao().buscarPorId(propiedadId)!=null) { "Propiedad inexistente." }
        val habitaciones=bd.habitacionDao().listarPorPropiedad(propiedadId).first()
        val ocupadas=bd.contratoDao().habitacionesOcupadas(fechaIso).first().toSet()
        val totalOcupadas=habitaciones.count { it.id in ocupadas }
        return Ocupacion(propiedadId,fechaIso,habitaciones.size,totalOcupadas,
            habitaciones.size-totalOcupadas)
    }

    /**
     * Movimientos por fecha, con cargo imputado al vencimiento de su renta.
     * Los cargos no tienen fecha independiente en el UML de 10 tablas.
     */
    suspend fun estadoCuenta(contratoId:Int): List<MovimientoCuenta> = bd.withTransaction {
        require(bd.contratoDao().buscarPorId(contratoId)!=null) { "Contrato inexistente." }
        data class Evento(val fecha:String,val orden:Int,val ref:Int,val nombre:String,val importe:Long)
        val rentas=bd.rentaDao().listarAhora(contratoId)
        val cargos=bd.cargoServicioDao().listarPorContratoAhora(contratoId)
            .filterNot { it.anulado }.groupBy { it.rentaId }
        val lista=mutableListOf<Evento>()
        for (renta in rentas) {
            lista += Evento(renta.fechaVencimiento,0,renta.id,
                "Renta " + renta.periodo,renta.rentaBaseCent)
            for (cargo in cargos[renta.id].orEmpty()) {
                lista += Evento(renta.fechaVencimiento,1,cargo.id,
                    "Cargo: " + cargo.tipo,cargo.montoCent)
            }
        }
        for (abono in bd.abonoDao().vigentesPorContrato(contratoId)) {
            lista += Evento(abono.fecha,2,abono.id,
                if (abono.origen=="GARANTIA") "Aplicación interna de garantía" else "Abono ("+abono.medio+")",
                Math.negateExact(abono.montoCent))
        }
        var acumulado=0L
        lista.sortedWith(compareBy({it.fecha},{it.orden},{it.ref})).map { evento ->
            acumulado=Math.addExact(acumulado,evento.importe)
            MovimientoCuenta(evento.fecha,evento.nombre,evento.importe,acumulado,evento.ref)
        }
    }

    /**
     * Calcula deuda vencida a una fecha usando importes y cargos actuales y abonos
     * que existían hasta esa fecha. No reemplaza una auditoría histórica de ediciones.
     */
    suspend fun deudasVencidas(fechaIso:String): List<DeudaVencida> = bd.withTransaction {
        val corte=LocalDate.parse(fechaIso)
        val resultado=mutableListOf<DeudaVencida>()
        for (contrato in bd.contratoDao().todosAhora()) {
            val rentas=bd.rentaDao().listarAhora(contrato.id)
            if (rentas.isEmpty()) continue
            val cargos=bd.cargoServicioDao().listarPorContratoAhora(contrato.id)
                .filterNot { it.anulado }.groupBy { it.rentaId }
            val partidas=rentas.map { r -> CalculosCobros.RentaPendiente(r.id,
                cargos[r.id].orEmpty().fold(r.rentaBaseCent) { acc,c -> Math.addExact(acc,c.montoCent) },
                r.fechaVencimiento) }
            val abonos=bd.abonoDao().vigentesPorContrato(contrato.id)
                .filter { it.fecha<=fechaIso }
                .map { CalculosCobros.AbonoDisponible(it.id,it.montoCent) }
            val aplicado=CalculosCobros.distribuir(partidas,abonos)
                .groupBy { it.rentaId }.mapValues { (_,lista) -> lista.sumOf { it.montoCent } }
            var vencido=0L
            for (renta in partidas) {
                if (LocalDate.parse(renta.vencimiento).isBefore(corte)) {
                    val saldo=Math.subtractExact(renta.exigibleCent, aplicado[renta.rentaId] ?: 0L)
                    vencido=Math.addExact(vencido,saldo)
                }
            }
            if (vencido>0L) resultado+=DeudaVencida(contrato.id,vencido)
        }
        resultado
    }

    suspend fun abonosEfectivos(propiedadId:Int,periodoIso:String): List<AbonoEntity> {
        val mes=YearMonth.parse(periodoIso)
        return bd.abonoDao().listarEfectivosPropiedadMes(propiedadId,
            mes.atDay(1).toString(),mes.plusMonths(1).atDay(1).toString())
    }
}
