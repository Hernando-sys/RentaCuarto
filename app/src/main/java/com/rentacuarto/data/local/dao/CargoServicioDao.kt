package com.rentacuarto.data.local.dao

import androidx.room.*
import com.rentacuarto.data.local.entity.CargoServicioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CargoServicioDao {
    @Insert suspend fun insertar(item: CargoServicioEntity): Long
    @Update suspend fun actualizar(item: CargoServicioEntity)
    @Query("SELECT * FROM cargos_servicio WHERE renta_id=:rentaId ORDER BY id")
    fun listarPorRenta(rentaId: Int): Flow<List<CargoServicioEntity>>
    @Query("SELECT * FROM cargos_servicio WHERE renta_id IN (SELECT id FROM rentas WHERE contrato_id=:contratoId) ORDER BY id")
    suspend fun listarPorContratoAhora(contratoId: Int): List<CargoServicioEntity>
    @Query("SELECT * FROM cargos_servicio WHERE id=:id LIMIT 1")
    suspend fun buscarPorId(id: Int): CargoServicioEntity?
}
