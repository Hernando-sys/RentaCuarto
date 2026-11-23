package com.rentacuarto.data.local.dao

import androidx.room.*
import com.rentacuarto.data.local.entity.RentaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RentaDao {
    @Insert suspend fun insertar(item: RentaEntity): Long
    @Query("SELECT * FROM rentas WHERE contrato_id=:contratoId ORDER BY periodo, id")
    fun listarPorContrato(contratoId: Int): Flow<List<RentaEntity>>
    @Query("SELECT * FROM rentas WHERE contrato_id=:contratoId ORDER BY periodo, id")
    suspend fun listarAhora(contratoId: Int): List<RentaEntity>
    @Query("SELECT * FROM rentas WHERE contrato_id=:contratoId AND periodo=:periodo LIMIT 1")
    suspend fun buscarPeriodo(contratoId: Int, periodo: String): RentaEntity?
    @Query("SELECT * FROM rentas WHERE id=:id LIMIT 1")
    suspend fun buscarPorId(id: Int): RentaEntity?
}
