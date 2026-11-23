package com.rentacuarto.data.repository

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.GastoEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

/** Registro, corrección y anulación lógica de gastos (RF15). */
class GastosRepository(private val bd: RentaCuartoDatabase) {
    private val dao=bd.gastoDao()
    private val categorias=setOf("Servicios básicos", "Impuestos y arbitrios",
        "Mantenimiento y reparaciones", "Limpieza", "Equipamiento", "Otros")

    fun listar(propiedadId: Int): Flow<List<GastoEntity>> = dao.listarPorPropiedad(propiedadId)

    suspend fun guardar(id: Int=0, propiedadId: Int, fecha: String, categoria: String,
                        descripcion: String, montoCent: Long): Long = bd.withTransaction {
        LocalDate.parse(fecha)
        require(bd.propiedadDao().buscarPorId(propiedadId)!=null) { "Propiedad inexistente." }
        require(categoria in categorias && descripcion.isNotBlank() && montoCent>0L) {
            "Categoría, descripción o monto inválido."
        }
        if (id==0) dao.insertar(GastoEntity(propiedadId=propiedadId,
            fecha=fecha,categoria=categoria,descripcion=descripcion.trim(),montoCent=montoCent))
        else {
            val original=dao.buscarPorId(id) ?: error("Gasto inexistente.")
            require(!original.anulado) { "No se puede editar un gasto anulado." }
            require(original.propiedadId==propiedadId) { "No se cambia el inmueble de un gasto." }
            dao.actualizar(original.copy(fecha=fecha,categoria=categoria,
                descripcion=descripcion.trim(),montoCent=montoCent))
            id.toLong()
        }
    }

    suspend fun anular(id: Int) = bd.withTransaction {
        val original=dao.buscarPorId(id) ?: error("Gasto inexistente.")
        require(!original.anulado) { "El gasto ya está anulado." }
        dao.actualizar(original.copy(anulado=true))
    }

    data class ResultadoMensual(val propiedadId:Int,val periodo:String,
        val abonosEfectivosCent:Long,val gastosCent:Long,val resultadoCent:Long,
        val gastosPorCategoria:Map<String,Long>)

    /** El abono interno con origen GARANTIA no se suma como ingreso. */
    suspend fun resultadoMensual(propiedadId: Int, periodoIso: String): ResultadoMensual = bd.withTransaction {
        require(bd.propiedadDao().buscarPorId(propiedadId)!=null) { "Propiedad inexistente." }
        val mes=YearMonth.parse(periodoIso)
        val desde=mes.atDay(1).toString()
        val hasta=mes.plusMonths(1).atDay(1).toString()
        val efectivo=bd.abonoDao().efectivoPropiedadMes(propiedadId,desde,hasta)
        val gastos=dao.listarVigentesMes(propiedadId,desde,hasta)
        val total=gastos.fold(0L) { acc,gasto -> Math.addExact(acc,gasto.montoCent) }
        val grupos=gastos.groupBy { it.categoria }.mapValues { (_,fila) ->
            fila.fold(0L) { acc,gasto -> Math.addExact(acc,gasto.montoCent) }
        }
        ResultadoMensual(propiedadId,mes.toString(),efectivo,total,
            CalculosCobros.resultadoOperativo(efectivo,total),grupos)
    }
}
