package com.rentacuarto.data.repository

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.YearMonth

/**
 * Núcleo de datos económicos (RF08–RF11, RF18, RF21).
 * Los métodos se exponen a futuro ViewModels; todavía no se crean pantallas.
 * Las transacciones y la redistribución evitan estados incoherentes.
 */
class CobrosRepository(private val bd: RentaCuartoDatabase) {
    private val contratos = bd.contratoDao()
    private val rentas = bd.rentaDao()
    private val cargos = bd.cargoServicioDao()
    private val abonos = bd.abonoDao()
    private val aplicaciones = bd.aplicacionAbonoDao()
    private val garantias = bd.garantiaDao()

    fun listarRentas(contratoId: Int): Flow<List<RentaEntity>> = rentas.listarPorContrato(contratoId)
    fun listarCargos(rentaId: Int): Flow<List<CargoServicioEntity>> = cargos.listarPorRenta(rentaId)
    fun listarAbonos(contratoId: Int): Flow<List<AbonoEntity>> = abonos.listarPorContrato(contratoId)
    fun listarAplicaciones(rentaId: Int): Flow<List<AplicacionAbonoEntity>> = aplicaciones.listarPorRenta(rentaId)
    fun observarGarantia(contratoId: Int): Flow<List<GarantiaEntity>> = garantias.observar(contratoId)

    /** Devuelve el ID existente si el contrato-periodo ya fue generado. */
    suspend fun generarRenta(contratoId: Int, periodoIso: String): Long = bd.withTransaction {
        val periodo = YearMonth.parse(periodoIso)
        val contrato = contratos.buscarPorId(contratoId)
            ?: throw IllegalArgumentException("Contrato inexistente.")
        val primer = LocalDate.parse(contrato.primerVencimiento)
        require(!periodo.isBefore(YearMonth.from(primer))) { "Periodo anterior al primer vencimiento." }
        val vencimiento = periodo.atDay(minOf(primer.dayOfMonth, periodo.lengthOfMonth()))
        require(!vencimiento.isBefore(LocalDate.parse(contrato.fechaInicio))) { "Periodo fuera del contrato." }
        require(contrato.fechaFinReal == null ||
            !vencimiento.isAfter(LocalDate.parse(contrato.fechaFinReal))) {
            "No se genera renta después del fin real del contrato."
        }
        rentas.buscarPeriodo(contratoId, periodo.toString())?.id?.toLong()
            ?: rentas.insertar(RentaEntity(contratoId=contratoId,periodo=periodo.toString(),
                fechaVencimiento=vencimiento.toString(),rentaBaseCent=contrato.rentaMensualCent))
    }

    /** Inserta un cargo y recalcula todas las aplicaciones del contrato. */
    suspend fun registrarCargo(rentaId: Int, tipo: String, descripcion: String, montoCent: Long): Long =
        bd.withTransaction {
            require(tipo.isNotBlank() && montoCent > 0L) { "Tipo e importe de cargo inválidos." }
            val renta = rentas.buscarPorId(rentaId) ?: error("Renta inexistente.")
            val id = cargos.insertar(CargoServicioEntity(rentaId=rentaId,
                tipo=tipo.trim(),descripcion=descripcion.trim(),montoCent=montoCent))
            redistribuirDentroDeTransaccion(renta.contratoId)
            id
        }

    suspend fun editarCargo(id: Int, tipo: String, descripcion: String, montoCent: Long) = bd.withTransaction {
        require(tipo.isNotBlank() && montoCent > 0L) { "Tipo e importe de cargo inválidos." }
        val actual = cargos.buscarPorId(id) ?: error("Cargo inexistente.")
        require(!actual.anulado) { "No se puede editar un cargo anulado." }
        cargos.actualizar(actual.copy(tipo=tipo.trim(),descripcion=descripcion.trim(),montoCent=montoCent))
        redistribuirDentroDeTransaccion(rentas.buscarPorId(actual.rentaId)!!.contratoId)
    }

    suspend fun anularCargo(id: Int) = bd.withTransaction {
        val actual = cargos.buscarPorId(id) ?: error("Cargo inexistente.")
        require(!actual.anulado) { "El cargo ya está anulado." }
        cargos.actualizar(actual.copy(anulado=true))
        redistribuirDentroDeTransaccion(rentas.buscarPorId(actual.rentaId)!!.contratoId)
    }

    suspend fun registrarAbono(contratoId: Int, fechaIso: String, montoCent: Long,
                              medio: String, pagador: String?=null): Long = bd.withTransaction {
        require(contratos.buscarPorId(contratoId) != null) { "Contrato inexistente." }
        require(montoCent > 0L && medio.isNotBlank() &&
            medio.trim().uppercase() !in setOf("INTERNO", "GARANTIA")) {
            "Importe o medio inválido para un abono efectivo."
        }
        LocalDate.parse(fechaIso)
        val id=abonos.insertar(AbonoEntity(contratoId=contratoId,fecha=fechaIso,
            montoCent=montoCent,medio=medio.trim().uppercase(),origen="EFECTIVO",
            pagador=pagador?.trim()?.ifEmpty { null }))
        redistribuirDentroDeTransaccion(contratoId)
        id
    }

    /** Corrección trazable: anula el movimiento previo, conserva su fila e inserta el nuevo. */
    suspend fun corregirAbono(id: Int, fechaIso: String, montoCent: Long,
                             medio: String, pagador: String?=null): Long = bd.withTransaction {
        val previo=abonos.buscarPorId(id) ?: error("Abono inexistente.")
        require(!previo.anulado && previo.origen=="EFECTIVO") {
            "Solo pueden corregirse abonos efectivos vigentes."
        }
        require(montoCent > 0L && medio.isNotBlank() &&
            medio.trim().uppercase() !in setOf("INTERNO", "GARANTIA")) {
            "Importe o medio inválido para un abono efectivo."
        }
        LocalDate.parse(fechaIso)
        abonos.actualizar(previo.copy(anulado=true))
        val nuevo=abonos.insertar(previo.copy(id=0,fecha=fechaIso,montoCent=montoCent,
            medio=medio.trim().uppercase(),pagador=pagador?.trim()?.ifEmpty { null },anulado=false))
        redistribuirDentroDeTransaccion(previo.contratoId)
        nuevo
    }

    suspend fun anularAbono(id: Int) = bd.withTransaction {
        val actual=abonos.buscarPorId(id) ?: error("Abono inexistente.")
        require(!actual.anulado && actual.origen=="EFECTIVO") {
            "No se puede anular un movimiento interno o ya anulado."
        }
        abonos.actualizar(actual.copy(anulado=true))
        redistribuirDentroDeTransaccion(actual.contratoId)
    }

    /** Registro de dinero recibido por garantía, distinto a un abono de renta. */
    suspend fun registrarRecepcionGarantia(contratoId: Int, montoCent: Long) = bd.withTransaction {
        require(montoCent > 0L) { "La recepción debe ser mayor a cero." }
        val garantia=garantias.buscarPorContrato(contratoId) ?: error("No hay garantía pactada.")
        require(!garantia.cerrado) { "La garantía está liquidada." }
        val suma=Math.addExact(garantia.recibidoCent,montoCent)
        require(suma <= garantia.pactadoCent) { "La recepción excede el pacto de garantía." }
        garantias.actualizar(garantia.copy(recibidoCent=suma))
    }

    /** Devolución y aplicación internas, sin contaminar los cobros EFECTIVOS. */
    suspend fun liquidarGarantia(contratoId: Int, devolverCent: Long, aplicarCent: Long) = bd.withTransaction {
        require(devolverCent >= 0L && aplicarCent >= 0L &&
            Math.addExact(devolverCent, aplicarCent) > 0L) {
            "Indica devolución o aplicación positiva."
        }
        val contrato=contratos.buscarPorId(contratoId) ?: error("Contrato inexistente.")
        require(contrato.fechaFinReal != null) { "Primero finaliza el contrato." }
        val garantia=garantias.buscarPorContrato(contratoId) ?: error("No hay garantía pactada.")
        require(!garantia.cerrado) { "La garantía ya fue liquidada." }
        val nuevoDevuelto=Math.addExact(garantia.devueltoCent,devolverCent)
        val nuevoAplicado=Math.addExact(garantia.aplicadoCent,aplicarCent)
        require(Math.addExact(nuevoDevuelto,nuevoAplicado) <= garantia.recibidoCent) {
            "La devolución y aplicación exceden el monto recibido."
        }
        if (aplicarCent>0L) {
            val rentasContrato=rentas.listarAhora(contratoId)
            val extras=cargos.listarPorContratoAhora(contratoId).filterNot { it.anulado }
            val exigible=rentasContrato.fold(0L) { acc,renta -> Math.addExact(acc,renta.rentaBaseCent) }
            val totalExigible=extras.fold(exigible) { acc,cargo -> Math.addExact(acc,cargo.montoCent) }
            val pagado=abonos.vigentesPorContrato(contratoId).fold(0L) { acc,abono ->
                Math.addExact(acc,abono.montoCent)
            }
            val deudaPendiente=maxOf(0L,Math.subtractExact(totalExigible,pagado))
            require(aplicarCent <= deudaPendiente) {
                "No se puede aplicar más garantía que la deuda registrada."
            }
        }
        garantias.actualizar(garantia.copy(devueltoCent=nuevoDevuelto,aplicadoCent=nuevoAplicado,
            cerrado=(Math.addExact(nuevoDevuelto,nuevoAplicado)==garantia.recibidoCent)))
        if (aplicarCent>0L) {
            abonos.insertar(AbonoEntity(contratoId=contratoId,garantiaId=garantia.id,
                fecha=LocalDate.now().toString(),montoCent=aplicarCent,
                medio="INTERNO",origen="GARANTIA"))
            redistribuirDentroDeTransaccion(contratoId)
        }
    }

    /**
     * Reparte los abonos cronológicamente a rentas antiguas.
     * Se invoca dentro de la misma transacción que crea/corrige el movimiento.
     */
    private suspend fun redistribuirDentroDeTransaccion(contratoId: Int) {
        val rentasOrdenadas=rentas.listarAhora(contratoId)
        val cargosVigentes=cargos.listarPorContratoAhora(contratoId)
            .filterNot { it.anulado }.groupBy { it.rentaId }
        val partidas=rentasOrdenadas.map { renta ->
            val importe=cargosVigentes[renta.id].orEmpty().fold(renta.rentaBaseCent) { acc,cargo ->
                Math.addExact(acc,cargo.montoCent)
            }
            CalculosCobros.RentaPendiente(renta.id,importe,renta.fechaVencimiento)
        }
        val pagos=abonos.vigentesPorContrato(contratoId).map {
            CalculosCobros.AbonoDisponible(it.id,it.montoCent)
        }
        val nuevas=CalculosCobros.distribuir(partidas,pagos)
        aplicaciones.limpiarContrato(contratoId)
        for (aplicacion in nuevas) {
            aplicaciones.insertar(AplicacionAbonoEntity(abonoId=aplicacion.abonoId,
                rentaId=aplicacion.rentaId,montoCent=aplicacion.montoCent))
        }
    }
}
