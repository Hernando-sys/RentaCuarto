package com.rentacuarto.data.repository

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.HabitacionEntity
import com.rentacuarto.data.local.entity.PropiedadEntity
import kotlinx.coroutines.flow.Flow

/** Un solo repositorio para las propiedades y sus habitaciones. */
class PropiedadesRepository(private val database: RentaCuartoDatabase) {
    private val propiedadDao = database.propiedadDao()
    private val habitacionDao = database.habitacionDao()
    private val contratoDao = database.contratoDao()

    fun listarPropiedades(): Flow<List<PropiedadEntity>> = propiedadDao.listar()
    fun listarPropiedadPorId(id: Int): Flow<List<PropiedadEntity>> = propiedadDao.listarPorId(id)
    fun listarHabitaciones(): Flow<List<HabitacionEntity>> = habitacionDao.listar()
    fun listarHabitacionesPorPropiedad(id: Int): Flow<List<HabitacionEntity>> =
        habitacionDao.listarPorPropiedad(id)
    fun listarHabitacionPorId(id: Int): Flow<List<HabitacionEntity>> = habitacionDao.listarPorId(id)
    fun habitacionesOcupadas(fecha: String): Flow<List<Int>> = contratoDao.habitacionesOcupadas(fecha)

    suspend fun guardarPropiedad(id: Int, nombre: String, direccion: String, tipo: String, notas: String) {
        val nombreLimpio = nombre.trim()
        val direccionLimpia = direccion.trim()
        require(nombreLimpio.isNotEmpty()) { "Ingresa el nombre de la propiedad." }
        require(direccionLimpia.isNotEmpty()) { "Ingresa la dirección." }
        require(tipo == "CASA" || tipo == "EDIFICIO") { "Selecciona el tipo de propiedad." }
        val propiedad = PropiedadEntity(id, nombreLimpio, direccionLimpia, tipo, notas.trim())
        if (id == 0) {
            propiedadDao.insertar(propiedad)
        } else {
            require(propiedadDao.buscarPorId(id) != null) { "La propiedad ya no existe." }
            propiedadDao.actualizar(propiedad)
        }
    }

    suspend fun eliminarPropiedad(propiedad: PropiedadEntity) {
        database.withTransaction {
            require(habitacionDao.contarPorPropiedad(propiedad.id) == 0) {
                "Primero debes gestionar las habitaciones de esta propiedad."
            }
            require(database.gastoDao().contarPorPropiedad(propiedad.id) == 0) {
                "La propiedad tiene gastos registrados y debe conservarse."
            }
            propiedadDao.eliminar(propiedad)
        }
    }

    suspend fun guardarHabitacion(id: Int, propiedadId: Int, codigo: String, descripcion: String) {
        val codigoLimpio = codigo.trim().uppercase()
        require(codigoLimpio.isNotEmpty()) { "Ingresa el código de la habitación." }
        require(propiedadDao.buscarPorId(propiedadId) != null) { "Selecciona una propiedad existente." }
        val repetida = habitacionDao.buscarPorCodigo(propiedadId, codigoLimpio)
        require(repetida == null || repetida.id == id) { "El código ya existe en esta propiedad." }
        val habitacion = HabitacionEntity(id, propiedadId, codigoLimpio, descripcion.trim())
        if (id == 0) {
            habitacionDao.insertar(habitacion)
        } else {
            val actual = habitacionDao.buscarPorId(id)
            require(actual != null) { "La habitación ya no existe." }
            // No se permite cambiar de propiedad si tiene contratos históricos.
            require(actual.propiedadId == propiedadId) {
                "No se puede trasladar una habitación a otra propiedad."
            }
            habitacionDao.actualizar(habitacion)
        }
    }

    suspend fun eliminarHabitacion(habitacion: HabitacionEntity) {
        database.withTransaction {
            require(contratoDao.contarPorHabitacion(habitacion.id) == 0) {
                "Esta habitación tiene contratos y debe conservarse en el historial."
            }
            habitacionDao.eliminar(habitacion)
        }
    }
}
