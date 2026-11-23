package com.rentacuarto.data.local.dao

import androidx.room.*
import com.rentacuarto.data.local.entity.AbonoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AbonoDao {
    @Insert suspend fun insertar(item: AbonoEntity): Long
    @Update suspend fun actualizar(item: AbonoEntity)
    @Query("SELECT * FROM abonos WHERE contrato_id=:contratoId ORDER BY fecha, id")
    fun listarPorContrato(contratoId: Int): Flow<List<AbonoEntity>>
    @Query("SELECT * FROM abonos WHERE contrato_id=:contratoId AND anulado=0 ORDER BY fecha, id")
    suspend fun vigentesPorContrato(contratoId: Int): List<AbonoEntity>
    @Query("SELECT * FROM abonos WHERE id=:id LIMIT 1")
    suspend fun buscarPorId(id: Int): AbonoEntity?
    @Query("SELECT a.* FROM abonos a JOIN contratos c ON c.id=a.contrato_id JOIN habitaciones h ON h.id=c.habitacion_id WHERE h.propiedad_id=:propiedadId AND a.fecha>=:desde AND a.fecha<:hasta AND a.origen='EFECTIVO' AND a.anulado=0 ORDER BY a.fecha,a.id")
    suspend fun listarEfectivosPropiedadMes(propiedadId: Int, desde: String, hasta: String): List<AbonoEntity>

    @Query("SELECT COALESCE(SUM(a.monto_cent),0) FROM abonos a JOIN contratos c ON c.id=a.contrato_id JOIN habitaciones h ON h.id=c.habitacion_id WHERE h.propiedad_id=:propiedadId AND a.fecha >= :desde AND a.fecha < :hasta AND a.origen='EFECTIVO' AND a.anulado=0")
    suspend fun efectivoPropiedadMes(propiedadId: Int, desde: String, hasta: String): Long
}
