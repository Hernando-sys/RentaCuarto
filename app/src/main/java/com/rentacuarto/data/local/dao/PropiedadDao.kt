package com.rentacuarto.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rentacuarto.data.local.entity.PropiedadEntity
import kotlinx.coroutines.flow.Flow

/** Las consultas Flow actualizan los listados al modificarse SQLite. */
@Dao
interface PropiedadDao {
    @Insert
    suspend fun insertar(propiedad: PropiedadEntity): Long

    @Update
    suspend fun actualizar(propiedad: PropiedadEntity)

    @Delete
    suspend fun eliminar(propiedad: PropiedadEntity)

    @Query("SELECT * FROM propiedades ORDER BY nombre")
    fun listar(): Flow<List<PropiedadEntity>>

    @Query("SELECT * FROM propiedades WHERE id = :id")
    fun listarPorId(id: Int): Flow<List<PropiedadEntity>>

    @Query("SELECT * FROM propiedades WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): PropiedadEntity?
}
