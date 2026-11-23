package com.rentacuarto.data.repository

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.ContratoEntity
import com.rentacuarto.util.DineroUtils
import com.rentacuarto.util.FechaUtils
import kotlinx.coroutines.flow.Flow

/**
 * Exposición P01-P12: registra y finaliza contratos.
 * Garantía y adelantos solo son condiciones pactadas; no se cobran aquí.
 */
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
            // Regla conservadora del primer avance: una habitación no puede tener
            // otro contrato sin fin real, aunque su fecha de inicio sea futura.
            require(contratoDao.contarSinFinalizar(habitacionId) == 0) {
                "La habitación ya tiene un contrato sin finalizar."
            }
            contratoDao.insertar(
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
            // El depósito pactado está representado en numeroRentasGarantia;
            // su recepción efectiva se desarrollará en la versión final.
        }
    }
}
