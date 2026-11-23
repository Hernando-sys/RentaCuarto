package com.rentacuarto.data.local.dao

import androidx.room.*
import com.rentacuarto.data.local.entity.AplicacionAbonoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AplicacionAbonoDao {
    @Insert suspend fun insertar(item: AplicacionAbonoEntity): Long
    @Query("DELETE FROM aplicaciones_abono WHERE abono_id IN (SELECT id FROM abonos WHERE contrato_id=:contratoId)")
    suspend fun limpiarContrato(contratoId: Int)
    @Query("SELECT * FROM aplicaciones_abono WHERE renta_id=:rentaId ORDER BY id")
    fun listarPorRenta(rentaId: Int): Flow<List<AplicacionAbonoEntity>>
    @Query("SELECT * FROM aplicaciones_abono WHERE abono_id IN (SELECT id FROM abonos WHERE contrato_id=:contratoId)")
    suspend fun listarContratoAhora(contratoId: Int): List<AplicacionAbonoEntity>
}
