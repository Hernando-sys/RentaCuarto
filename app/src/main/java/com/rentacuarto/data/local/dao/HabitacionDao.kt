package com.rentacuarto.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rentacuarto.data.local.entity.HabitacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitacionDao {
    @Insert
    suspend fun insertar(habitacion: HabitacionEntity): Long

    @Update
    suspend fun actualizar(habitacion: HabitacionEntity)

    @Delete
    suspend fun eliminar(habitacion: HabitacionEntity)

    @Query("SELECT * FROM habitaciones ORDER BY propiedad_id, codigo")
    fun listar(): Flow<List<HabitacionEntity>>

    @Query("SELECT * FROM habitaciones WHERE propiedad_id = :propiedadId ORDER BY codigo")
    fun listarPorPropiedad(propiedadId: Int): Flow<List<HabitacionEntity>>

    @Query("SELECT * FROM habitaciones WHERE id = :id")
    fun listarPorId(id: Int): Flow<List<HabitacionEntity>>

    @Query("SELECT * FROM habitaciones WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): HabitacionEntity?

    @Query("SELECT * FROM habitaciones WHERE propiedad_id = :propiedadId AND codigo = :codigo LIMIT 1")
    suspend fun buscarPorCodigo(propiedadId: Int, codigo: String): HabitacionEntity?

    @Query("SELECT COUNT(*) FROM habitaciones WHERE propiedad_id = :propiedadId")
    suspend fun contarPorPropiedad(propiedadId: Int): Int
}
