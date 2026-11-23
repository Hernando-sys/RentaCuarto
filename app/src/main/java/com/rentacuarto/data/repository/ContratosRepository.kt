package com.rentacuarto.data.repository

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.ContratoEntity
import com.rentacuarto.data.local.entity.GarantiaEntity
import com.rentacuarto.util.DineroUtils
import com.rentacuarto.util.FechaUtils
import kotlinx.coroutines.flow.Flow

/** Registro y cierre contractual; garantiza atomicidad con el pacto de depósito. */
class ContratosRepository(private val database: RentaCuartoDatabase) {
    private val contratoDao = database.contratoDao()
    private val habitacionDao = database.habitacionDao()
    private val inquilinoDao = database.inquilinoDao()

    fun listar(): Flow<List<ContratoEntity>> = contratoDao.listar()
    fun listarPorId(id: Int): Flow<List<ContratoEntity>> = contratoDao.listarPorId(id)

    suspend fun finalizar(id: Int, fechaFinTexto: String) {
        val fechaFin = FechaUtils.leer(fechaFinTexto, "La fecha de fin real")
        database.withTransaction {
            val contrato = contratoDao.buscarPorId(id)
                ?: throw IllegalArgumentException("El contrato no existe.")
            require(contrato.fechaFinReal == null) { "El contrato ya fue finalizado." }
            val inicio = FechaUtils.leer(contrato.fechaInicio, "La fecha de inicio")
            require(!fechaFin.isBefore(inicio)) { "La fecha de fin no puede ser anterior al inicio." }
            require(!fechaFin.isAfter(java.time.LocalDate.now())) { "La fecha de fin real no puede ser futura." }
            contratoDao.finalizar(id, fechaFin.toString())
        }
    }

    suspend fun registrar(
        habitacionId: Int,
        inquilinoId: Int,
        fechaInicio: String,
        fechaPrimerVencimiento: String,
        fechaFinPactada: String,
        rentaTexto: String,
        numeroGarantiasTexto: String,
        numeroAdelantosTexto: String
    ) {
        val inicio = FechaUtils.leer(fechaInicio, "La fecha de inicio")
        val vencimiento = FechaUtils.leer(fechaPrimerVencimiento, "El primer vencimiento")
        require(!vencimiento.isBefore(inicio)) { "El primer vencimiento no debe ser anterior al inicio." }
        val finPactado = if (fechaFinPactada.isBlank()) null else
            FechaUtils.leer(fechaFinPactada, "La fecha de fin pactada")
        require(finPactado == null || !finPactado.isBefore(inicio)) {
            "La fecha de fin pactada no puede ser anterior al inicio."
        }
        val rentaCentimos = DineroUtils.convertirACentimos(rentaTexto)
        val numeroGarantias = numeroGarantiasTexto.trim().toIntOrNull()
        val numeroAdelantos = numeroAdelantosTexto.trim().toIntOrNull()
        require(numeroGarantias != null && numeroGarantias >= 0) {
            "Las rentas en garantía deben ser un número entero no negativo."
        }
        require(numeroAdelantos != null && numeroAdelantos >= 0) {
            "Las rentas adelantadas deben ser un número entero no negativo."
        }
        database.withTransaction {
            require(habitacionDao.buscarPorId(habitacionId) != null) { "Selecciona una habitación válida." }
            require(inquilinoDao.buscarPorId(inquilinoId) != null) { "Selecciona un inquilino válido." }
            // Con fecha fin real conocida, la habitación se libera en ese día.
            // Un contrato sin fin real no permite agendar otro contrato superpuesto.
            val hayConflicto = contratoDao.contratosHabitacionAhora(habitacionId).any { previo ->
                previo.fechaFinReal == null || previo.fechaFinReal > inicio.toString()
            }
            require(!hayConflicto) { "La habitación ya tiene un contrato que ocupa este periodo." }
            val contratoId = contratoDao.insertar(
                ContratoEntity(
                    habitacionId = habitacionId,
                    inquilinoId = inquilinoId,
                    fechaInicio = inicio.toString(),
                    fechaFinPactada = finPactado?.toString(),
                    primerVencimiento = vencimiento.toString(),
                    rentaMensualCent = rentaCentimos,
                    numeroRentasGarantia = numeroGarantias,
                    numeroRentasAdelantadas = numeroAdelantos
                )
            )
            if (numeroGarantias > 0) {
                database.garantiaDao().insertar(GarantiaEntity(
                    contratoId=contratoId.toInt(),
                    pactadoCent=Math.multiplyExact(rentaCentimos,numeroGarantias.toLong()),
                    recibidoCent=0L
                ))
            }
            // Pacto y recepción son distintos: el alta no registra ningún cobro.
        }
    }
}
