package com.rentacuarto.data.local.dao

import androidx.room.*
import com.rentacuarto.data.local.entity.GarantiaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GarantiaDao {
    @Insert suspend fun insertar(item: GarantiaEntity): Long
    @Update suspend fun actualizar(item: GarantiaEntity)
    @Query("SELECT * FROM garantias WHERE contrato_id=:contratoId LIMIT 1")
    suspend fun buscarPorContrato(contratoId: Int): GarantiaEntity?
    @Query("SELECT * FROM garantias WHERE contrato_id=:contratoId")
    fun observar(contratoId: Int): Flow<List<GarantiaEntity>>
}
