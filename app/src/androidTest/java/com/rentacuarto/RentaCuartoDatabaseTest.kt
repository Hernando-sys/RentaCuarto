package com.rentacuarto

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.local.entity.*
import com.rentacuarto.data.repository.ContratosRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RentaCuartoDatabaseTest {
    private lateinit var bd: RentaCuartoDatabase

    @Before fun preparar() {
        val contexto=InstrumentationRegistry.getInstrumentation().targetContext
        bd=Room.inMemoryDatabaseBuilder(contexto, RentaCuartoDatabase::class.java).build()
    }
    @After fun cerrar() = bd.close()

    @Test fun insertaPropiedadYHabitacion() = runBlocking {
        val propiedad=bd.propiedadDao().insertar(PropiedadEntity(
            nombre="Casa DEMO",direccion="Calle 1",tipo="CASA")).toInt()
        bd.habitacionDao().insertar(HabitacionEntity(propiedadId=propiedad,codigo="101"))
        assertEquals(1,bd.propiedadDao().listar().first().size)
        assertEquals(1,bd.habitacionDao().listarPorPropiedad(propiedad).first().size)
    }

    @Test fun contratoOcupaYLaFinalizacionLiberaHabitacion() = runBlocking {
        val propiedad=bd.propiedadDao().insertar(PropiedadEntity(
            nombre="Casa DEMO",direccion="Calle 1",tipo="CASA")).toInt()
        val room=bd.habitacionDao().insertar(HabitacionEntity(propiedadId=propiedad,codigo="101")).toInt()
        val persona=bd.inquilinoDao().insertar(InquilinoEntity(
            nombres="Persona Demo",dni="90000001",telefono="900000001")).toInt()
        val repositorio=ContratosRepository(bd)
        repositorio.registrar(room,persona,"2026-01-01","2026-01-10","","500","1","0")
        assertEquals(1,bd.contratoDao().contarSinFinalizar(room))
        assertTrue(bd.contratoDao().habitacionesOcupadas("2026-03-01").first().contains(room))
        val id=bd.contratoDao().listar().first().first().id
        repositorio.finalizar(id,"2026-02-20")
        assertTrue(!bd.contratoDao().habitacionesOcupadas("2026-03-01").first().contains(room))
    }

    @Test fun generaRentaConCargoYDosAbonosSinDuplicarla() = runBlocking {
        val propiedad=bd.propiedadDao().insertar(PropiedadEntity(
            nombre="Casa",direccion="Calle 1",tipo="CASA")).toInt()
        val habitacion=bd.habitacionDao().insertar(HabitacionEntity(
            propiedadId=propiedad,codigo="101")).toInt()
        val inquilino=bd.inquilinoDao().insertar(InquilinoEntity(
            nombres="Persona Test",dni="90000002",telefono="900000002")).toInt()
        val contratos=com.rentacuarto.data.repository.ContratosRepository(bd)
        contratos.registrar(habitacion,inquilino,"2026-01-01","2026-01-10","","500","1","0")
        val idContrato=bd.contratoDao().listar().first().first().id
        val cobros=com.rentacuarto.data.repository.CobrosRepository(bd)
        val primera=cobros.generarRenta(idContrato,"2026-01").toInt()
        val segunda=cobros.generarRenta(idContrato,"2026-01").toInt()
        assertEquals(primera,segunda)
        assertEquals(1,bd.rentaDao().listarAhora(idContrato).size)
        cobros.registrarCargo(primera,"Agua","Consumo",3000)
        cobros.registrarAbono(idContrato,"2026-01-11",20000,"YAPE")
        cobros.registrarAbono(idContrato,"2026-01-12",33000,"EFECTIVO")
        val total=bd.aplicacionAbonoDao().listarContratoAhora(idContrato).sumOf { it.montoCent }
        assertEquals(53000L,total)
        assertEquals(50000L,bd.garantiaDao().buscarPorContrato(idContrato)?.pactadoCent ?: -1L)
        assertEquals(0L,bd.garantiaDao().buscarPorContrato(idContrato)?.recibidoCent ?: -1L)
    }

    @Test fun gastosYResultadoExcluyenAplicacionInternaGarantia() = runBlocking {
        val p=bd.propiedadDao().insertar(PropiedadEntity(
            nombre="Casa",direccion="Calle 1",tipo="CASA")).toInt()
        val h=bd.habitacionDao().insertar(HabitacionEntity(propiedadId=p,codigo="202")).toInt()
        val t=bd.inquilinoDao().insertar(InquilinoEntity(nombres="Persona",dni="90000003",telefono="900000003")).toInt()
        com.rentacuarto.data.repository.ContratosRepository(bd).registrar(
            h,t,"2026-01-01","2026-01-10","","500","1","0")
        val c=bd.contratoDao().listar().first().first().id
        val cobros=com.rentacuarto.data.repository.CobrosRepository(bd)
        cobros.generarRenta(c,"2026-01")
        cobros.registrarAbono(c,"2026-01-11",100000,"EFECTIVO")
        val gastos=com.rentacuarto.data.repository.GastosRepository(bd)
        gastos.guardar(propiedadId=p,fecha="2026-01-15",categoria="Limpieza",
            descripcion="Limpieza general",montoCent=27000)
        val res=gastos.resultadoMensual(p,"2026-01")
        assertEquals(100000L,res.abonosEfectivosCent)
        assertEquals(27000L,res.gastosCent)
        assertEquals(73000L,res.resultadoCent)
    }
}
