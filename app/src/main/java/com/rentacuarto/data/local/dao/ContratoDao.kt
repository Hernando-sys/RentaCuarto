package com.rentacuarto.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.rentacuarto.data.local.entity.ContratoEntity
import kotlinx.coroutines.flow.Flow

/** No se elimina un contrato: es parte del historial de la habitación. */
@Dao
interface ContratoDao {
    @Insert
    suspend fun insertar(contrato: ContratoEntity): Long

    @Query("SELECT * FROM contratos ORDER BY id DESC")
    fun listar(): Flow<List<ContratoEntity>>

    @Query("SELECT * FROM contratos WHERE id = :id")
    fun listarPorId(id: Int): Flow<List<ContratoEntity>>

    @Query("SELECT COUNT(*) FROM contratos WHERE habitacion_id = :habitacionId AND fin_real IS NULL")
    suspend fun contarSinFinalizar(habitacionId: Int): Int

    @Query("SELECT COUNT(*) FROM contratos WHERE habitacion_id = :habitacionId")
    suspend fun contarPorHabitacion(habitacionId: Int): Int

    @Query("SELECT COUNT(*) FROM contratos WHERE inquilino_id = :inquilinoId")
    suspend fun contarPorInquilino(inquilinoId: Int): Int

    // Disponible = sin contrato que esté vigente en la fecha consultada.
    @Query("""
        SELECT DISTINCT habitacion_id FROM contratos
        WHERE fecha_inicio <= :fecha
        AND (fin_real IS NULL OR fin_real > :fecha)
    """)
    fun habitacionesOcupadas(fecha: String): Flow<List<Int>>

    @Query("SELECT * FROM contratos WHERE id=:id LIMIT 1")
    suspend fun buscarPorId(id: Int): ContratoEntity?

    @Query("UPDATE contratos SET fin_real=:fechaFin WHERE id=:contratoId")
    suspend fun finalizar(contratoId: Int, fechaFin: String)

    @Query("SELECT * FROM contratos WHERE habitacion_id=:habitacionId ORDER BY fecha_inicio DESC")
    fun historialHabitacion(habitacionId: Int): Flow<List<ContratoEntity>>

    @Query("SELECT * FROM contratos WHERE inquilino_id=:inquilinoId ORDER BY fecha_inicio DESC")
    fun historialInquilino(inquilinoId: Int): Flow<List<ContratoEntity>>
}
