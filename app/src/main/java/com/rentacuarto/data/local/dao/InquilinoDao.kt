package com.rentacuarto.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rentacuarto.data.local.entity.InquilinoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InquilinoDao {
    @Insert
    suspend fun insertar(inquilino: InquilinoEntity): Long

    @Update
    suspend fun actualizar(inquilino: InquilinoEntity)

    @Delete
    suspend fun eliminar(inquilino: InquilinoEntity)

    @Query("SELECT * FROM inquilinos ORDER BY nombres")
    fun listar(): Flow<List<InquilinoEntity>>

    @Query("SELECT * FROM inquilinos WHERE id = :id")
    fun listarPorId(id: Int): Flow<List<InquilinoEntity>>

    @Query("SELECT * FROM inquilinos WHERE id = :id LIMIT 1")
    suspend fun buscarPorId(id: Int): InquilinoEntity?

    @Query("SELECT * FROM inquilinos WHERE dni = :dni LIMIT 1")
    suspend fun buscarPorDni(dni: String): InquilinoEntity?
}
