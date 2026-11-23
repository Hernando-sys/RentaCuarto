package com.rentacuarto.demo

import androidx.room.withTransaction
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.*
import kotlinx.coroutines.flow.first
import java.time.LocalDate

/** Muestra ficticia exclusiva de compilaciones debug. Si la BD tiene propiedades, NO se modifica. */
object DatosDesarrollo {
    suspend fun cargar(db: RentaCuartoDatabase) {
        db.withTransaction {
            if (db.propiedadDao().listar().first().isNotEmpty()) return@withTransaction
            val p1=db.propiedadDao().insertar(PropiedadEntity(
                nombre="Casa San Miguel (DEMO)",direccion="Av. Ejemplo 120, Lima",
                tipo="CASA",notas="Solo demostración")).toInt()
            val p2=db.propiedadDao().insertar(PropiedadEntity(
                nombre="Edificio Central (DEMO)",direccion="Calle Muestra 450, Lima",
                tipo="EDIFICIO",notas="Solo demostración")).toInt()
            val habitaciones=listOf(
                HabitacionEntity(propiedadId=p1,codigo="101",descripcion="Ventana exterior"),
                HabitacionEntity(propiedadId=p1,codigo="102",descripcion="Habitación amplia"),
                HabitacionEntity(propiedadId=p1,codigo="103",descripcion="Sin contrato vigente"),
                HabitacionEntity(propiedadId=p2,codigo="201",descripcion="Segundo nivel"),
                HabitacionEntity(propiedadId=p2,codigo="202",descripcion="Segundo nivel"),
                HabitacionEntity(propiedadId=p2,codigo="203",descripcion="Disponible")
            )
            val idsRoom=habitaciones.map { db.habitacionDao().insertar(it).toInt() }
            val personas=listOf(
                "Andrea Rojas Demo", "Carlos Pérez Demo", "Lucía Torres Demo",
                "Paola Flores Demo", "Mateo Salazar Demo"
            )
            val idsPersona=personas.mapIndexed { i,nombre ->
                db.inquilinoDao().insertar(InquilinoEntity(
                    nombres=nombre,dni="9000000${i+1}",telefono="90000000${i+1}"
                )).toInt()
            }
            val hoy=LocalDate.now()
            val mes=hoy.withDayOfMonth(1).minusMonths(4)
            val contratos=listOf(
                ContratoEntity(habitacionId=idsRoom[0],inquilinoId=idsPersona[0],
                    fechaInicio=mes.toString(),primerVencimiento=mes.plusDays(9).toString(),
                    fechaFinPactada=hoy.plusDays(12).toString(),rentaMensualCent=50000,
                    numeroRentasGarantia=1),
                ContratoEntity(habitacionId=idsRoom[1],inquilinoId=idsPersona[1],
                    fechaInicio=mes.toString(),primerVencimiento=mes.plusDays(9).toString(),
                    fechaFinPactada=hoy.plusMonths(2).toString(),rentaMensualCent=65000,
                    numeroRentasGarantia=1,numeroRentasAdelantadas=1),
                ContratoEntity(habitacionId=idsRoom[3],inquilinoId=idsPersona[2],
                    fechaInicio=mes.toString(),primerVencimiento=mes.plusDays(9).toString(),
                    fechaFinPactada=hoy.plusDays(28).toString(),rentaMensualCent=85000,
                    numeroRentasGarantia=2),
                ContratoEntity(habitacionId=idsRoom[4],inquilinoId=idsPersona[3],
                    fechaInicio=mes.minusMonths(4).toString(),
                    primerVencimiento=mes.minusMonths(4).plusDays(9).toString(),
                    fechaFinPactada=hoy.minusDays(10).toString(),
                    fechaFinReal=hoy.minusDays(10).toString(),
                    rentaMensualCent=73000,numeroRentasGarantia=1)
            )
            contratos.forEach { contrato ->
                val contratoId = db.contratoDao().insertar(contrato).toInt()
                if (contrato.numeroRentasGarantia > 0) {
                    db.garantiaDao().insertar(GarantiaEntity(
                        contratoId = contratoId,
                        pactadoCent = Math.multiplyExact(contrato.rentaMensualCent,
                            contrato.numeroRentasGarantia.toLong()),
                        recibidoCent = 0L
                    ))
                }
            }
        }
    }
}
