package com.rentacuarto.data.local.dao

import androidx.room.*
import com.rentacuarto.data.local.entity.GastoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Insert suspend fun insertar(item: GastoEntity): Long
    @Update suspend fun actualizar(item: GastoEntity)
    @Query("SELECT * FROM gastos WHERE propiedad_id=:propiedadId ORDER BY fecha DESC, id DESC")
    fun listarPorPropiedad(propiedadId: Int): Flow<List<GastoEntity>>
    @Query("SELECT * FROM gastos WHERE id=:id LIMIT 1")
    suspend fun buscarPorId(id: Int): GastoEntity?
    @Query("SELECT COUNT(*) FROM gastos WHERE propiedad_id=:propiedadId")
    suspend fun contarPorPropiedad(propiedadId: Int): Int
    @Query("SELECT COALESCE(SUM(monto_cent),0) FROM gastos WHERE propiedad_id=:propiedadId AND anulado=0 AND fecha>=:desde AND fecha<:hasta")
    suspend fun totalVigenteMes(propiedadId: Int, desde: String, hasta: String): Long
    @Query("SELECT * FROM gastos WHERE propiedad_id=:propiedadId AND anulado=0 AND fecha>=:desde AND fecha<:hasta ORDER BY categoria, fecha")
    suspend fun listarVigentesMes(propiedadId: Int, desde: String, hasta: String): List<GastoEntity>
}
