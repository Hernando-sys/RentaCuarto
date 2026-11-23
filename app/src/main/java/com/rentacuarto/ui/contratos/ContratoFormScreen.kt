package com.rentacuarto.ui.contratos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.propiedades.HabitacionesViewModel
import com.rentacuarto.ui.propiedades.PropiedadesViewModel
import com.rentacuarto.util.FechaUtils

/** P10: contrato vinculado a habitación e inquilino. */
@Composable
fun ContratoFormScreen(
    habitacionInicialId: Int,
    contratosViewModel: ContratosViewModel,
    habitacionesViewModel: HabitacionesViewModel,
    inquilinosViewModel: InquilinosViewModel,
    propiedadesViewModel: PropiedadesViewModel,
    onVolver: () -> Unit,
    onNuevoInquilino: () -> Unit = {}
) {
    val habitaciones by habitacionesViewModel.habitaciones.collectAsState()
    val ocupadas by habitacionesViewModel.idsOcupadas.collectAsState()
    val inquilinos by inquilinosViewModel.inquilinos.collectAsState()
    val propiedades by propiedadesViewModel.propiedades.collectAsState()
    var habitacionId by rememberSaveable(habitacionInicialId) { mutableIntStateOf(habitacionInicialId) }
    var inquilinoId by rememberSaveable { mutableIntStateOf(0) }
    var abrirHabitaciones by rememberSaveable { mutableStateOf(false) }
    var abrirInquilinos by rememberSaveable { mutableStateOf(false) }
    var fechaInicio by rememberSaveable { mutableStateOf(FechaUtils.hoy()) }
    var primerVencimiento by rememberSaveable { mutableStateOf(FechaUtils.hoy()) }
    var finPactada by rememberSaveable { mutableStateOf("") }
    var renta by rememberSaveable { mutableStateOf("") }
    var garantias by rememberSaveable { mutableStateOf("1") }
    var adelantadas by rememberSaveable { mutableStateOf("0") }
    var mensaje by rememberSaveable { mutableStateOf("") }
    val disponibles = habitaciones.filter { it.id !in ocupadas }
    val habitacion = habitaciones.find { it.id == habitacionId }
    val inquilino = inquilinos.find { it.id == inquilinoId }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Condiciones del contrato", "Selecciona una habitación disponible y un inquilino")
            TextoSecundario("Habitación")
            Box {
                BotonBorde(
                    if (habitacion == null) "Seleccionar habitación" else {
                        val propiedad = propiedades.find { it.id == habitacion.propiedadId }?.nombre ?: "Propiedad"
                        "$propiedad · Hab. ${habitacion.codigo}"
                    }, { abrirHabitaciones = true }, Modifier.fillMaxWidth())
                DropdownMenu(abrirHabitaciones, onDismissRequest = { abrirHabitaciones = false }) {
                    disponibles.forEach { disponible ->
                        DropdownMenuItem(text = { Text("Hab. ${disponible.codigo}") },
                            onClick = { habitacionId = disponible.id; abrirHabitaciones = false })
                    }
                }
            }
            TextoSecundario("Inquilino")
            Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                Box(Modifier.weight(1f)) {
                    BotonBorde(inquilino?.nombres ?: "Seleccionar inquilino", { abrirInquilinos = true }, Modifier.fillMaxWidth())
                    DropdownMenu(abrirInquilinos, onDismissRequest = { abrirInquilinos = false }) {
                        inquilinos.forEach { persona ->
                            DropdownMenuItem(text = { Text(persona.nombres) }, onClick = {
                                inquilinoId = persona.id; abrirInquilinos = false
                            })
                        }
                    }
                }
                BotonBorde("+ Nuevo", onNuevoInquilino)
            }
            CampoTexto("Renta mensual (S/)", renta, { renta = it }, tipoTeclado = KeyboardType.Decimal)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { CampoTexto("Primer vencimiento (AAAA-MM-DD)", primerVencimiento, { primerVencimiento = it }) }
                Box(Modifier.weight(1f)) { CampoTexto("Fecha de inicio (AAAA-MM-DD)", fechaInicio, { fechaInicio = it }) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { CampoTexto("Fin pactado (opcional)", finPactada, { finPactada = it }) }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { CampoTexto("Rentas en garantía", garantias, { garantias = it }, tipoTeclado = KeyboardType.Number) }
                Box(Modifier.weight(1f)) { CampoTexto("Rentas adelantadas", adelantadas, { adelantadas = it }, tipoTeclado = KeyboardType.Number) }
            }
            MensajeInformativo("El depósito en garantía y las rentas adelantadas son condiciones del contrato; no registran automáticamente un abono.")
            if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(9.dp))
        AccionesFormulario(onVolver, {
            contratosViewModel.registrar(habitacionId, inquilinoId, fechaInicio,
                primerVencimiento, finPactada, renta, garantias, adelantadas) { error ->
                if (error == null) onVolver() else mensaje = error
            }
        }, "Guardar contrato")
    }
}
