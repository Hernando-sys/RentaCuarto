package com.rentacuarto.ui.contratos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.propiedades.HabitacionesViewModel
import com.rentacuarto.ui.propiedades.PropiedadesViewModel
import com.rentacuarto.util.DineroUtils
import com.rentacuarto.util.FechaUtils

/** P11: solo condiciones contractuales, sin cobros ni garantías recibidas. */
@Composable
fun ContratoDetalleScreen(
    idContrato: Int, contratosViewModel: ContratosViewModel,
    habitacionesViewModel: HabitacionesViewModel,
    inquilinosViewModel: InquilinosViewModel,
    propiedadesViewModel: PropiedadesViewModel,
    onFinalizar: () -> Unit
) {
    val flujo = remember(idContrato) { contratosViewModel.listarPorId(idContrato) }
    val contratos by flujo.collectAsState(initial=emptyList())
    val habitaciones by habitacionesViewModel.habitaciones.collectAsState()
    val inquilinos by inquilinosViewModel.inquilinos.collectAsState()
    val propiedades by propiedadesViewModel.propiedades.collectAsState()
    val contrato = contratos.firstOrNull()

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement=Arrangement.spacedBy(12.dp)) {
        if (contrato == null) {
            EstadoVacio("Buscando contrato", "Comprueba que el contrato exista.")
            return@Column
        }
        val inquilino = inquilinos.find { it.id == contrato.inquilinoId }
        val habitacion = habitaciones.find { it.id == contrato.habitacionId }
        val propiedad = propiedades.find { it.id == habitacion?.propiedadId }
        val enFecha = contrato.fechaFinReal == null && contrato.fechaInicio <= FechaUtils.hoy()
        val estado = when {
            contrato.fechaFinReal != null -> "Finalizado"
            enFecha -> "Vigente"
            else -> "Programado"
        }
        EncabezadoContenido("Información del contrato", "Condiciones pactadas del alquiler")
        TarjetaRenta {
            Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
                Column(Modifier.weight(1f)) {
                    TituloSeccion(inquilino?.nombres ?: "Inquilino")
                    TextoSecundario("DNI: ${inquilino?.dni ?: "Sin datos"}")
                }
                EtiquetaEstado(estado, positivo=enFecha)
            }
            HorizontalDivider(Modifier.padding(vertical=8.dp))
            TextoSecundario("Propiedad: ${propiedad?.nombre ?: "No encontrada"}")
            TextoSecundario("Habitación: ${habitacion?.codigo ?: "No encontrada"}")
            Spacer(Modifier.height(9.dp))
            TituloSeccion("Renta mensual: ${DineroUtils.mostrar(contrato.rentaMensualCent)}")
            TextoSecundario("Fecha de inicio: ${contrato.fechaInicio}")
            TextoSecundario("Primer vencimiento: ${contrato.primerVencimiento}")
            TextoSecundario("Fin pactado: ${contrato.fechaFinPactada ?: "Sin fecha pactada"}")
            TextoSecundario("Fin real: ${contrato.fechaFinReal ?: "Aún no finalizado"}")
            TextoSecundario("Rentas pactadas en garantía: ${contrato.numeroRentasGarantia}")
            TextoSecundario("Rentas adelantadas pactadas: ${contrato.numeroRentasAdelantadas}")
        }
        MensajeInformativo("Las condiciones del contrato están disponibles. La interfaz de cobros y la liquidación de garantía siguen en desarrollo.")
        if (contrato.fechaFinReal == null)
            BotonBorde("Finalizar contrato", onFinalizar,
                Modifier.fillMaxWidth(), peligro=true)
    }
}
