package com.rentacuarto.data.repository

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.InquilinoEntity
import kotlinx.coroutines.flow.Flow

class InquilinosRepository(private val database: RentaCuartoDatabase) {
    private val inquilinoDao = database.inquilinoDao()
    private val contratoDao = database.contratoDao()

    fun listar(): Flow<List<InquilinoEntity>> = inquilinoDao.listar()
    fun listarPorId(id: Int): Flow<List<InquilinoEntity>> = inquilinoDao.listarPorId(id)

    suspend fun guardar(id: Int, nombres: String, dni: String, telefono: String) {
        val nombresLimpios = nombres.trim()
        val dniLimpio = dni.trim()
        val telefonoLimpio = telefono.trim()
        require(nombresLimpios.isNotEmpty()) { "Ingresa los nombres del inquilino." }
        require(dniLimpio.matches(Regex("[0-9]{8}"))) { "El DNI debe contener exactamente 8 dígitos." }
        require(telefonoLimpio.isNotEmpty()) { "Ingresa un teléfono." }
        val repetido = inquilinoDao.buscarPorDni(dniLimpio)
        require(repetido == null || repetido.id == id) { "Ya existe un inquilino con ese DNI." }
        val inquilino = InquilinoEntity(id, nombresLimpios, dniLimpio, telefonoLimpio)
        if (id == 0) {
            inquilinoDao.insertar(inquilino)
        } else {
            require(inquilinoDao.buscarPorId(id) != null) { "El inquilino ya no existe." }
            inquilinoDao.actualizar(inquilino)
        }
    }

    suspend fun eliminar(inquilino: InquilinoEntity) {
        database.withTransaction {
            require(contratoDao.contarPorInquilino(inquilino.id) == 0) {
                "No puedes eliminar un inquilino con contratos históricos."
            }
            inquilinoDao.eliminar(inquilino)
        }
    }
}
