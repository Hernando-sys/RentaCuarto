package com.rentacuarto.ui.contratos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.propiedades.HabitacionesViewModel
import com.rentacuarto.util.DineroUtils
import com.rentacuarto.ui.theme.*

/** P09. Filtros y tarjetas del módulo Contratos, con datos persistentes. */
@Composable
fun ContratosScreen(
    contratosViewModel: ContratosViewModel,
    habitacionesViewModel: HabitacionesViewModel,
    inquilinosViewModel: InquilinosViewModel,
    onCrear: () -> Unit,
    onInquilinos: () -> Unit,
    onDetalle: (Int) -> Unit
) {
    val contratos by contratosViewModel.contratos.collectAsState()
    val habitaciones by habitacionesViewModel.habitaciones.collectAsState()
    val inquilinos by inquilinosViewModel.inquilinos.collectAsState()
    var filtro by remember { mutableStateOf("Vigentes") }
    val visibles = contratos.filter { when(filtro) {
        "Vigentes" -> it.fechaFinReal == null
        "Finalizados" -> it.fechaFinReal != null
        else -> true
    } }
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(horizontal = 17.dp, vertical = 17.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Contratos de alquiler", "Consulta los contratos y su situación")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                BotonPrincipal("Contratos", {}, Modifier.weight(1f))
                BotonBorde("Inquilinos", onInquilinos, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Vigentes", "Finalizados", "Todos").forEach { opcion ->
                    FilterChip(selected = filtro == opcion, onClick = { filtro = opcion },
                        label = { Text(opcion) }, shape = RoundedCornerShape(24.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TextoRenta, selectedLabelColor = Color.White,
                            containerColor = BlancoRenta))
                }
            }
            TextoSecundario("${visibles.size} contratos en esta categoría")
            if (visibles.isEmpty()) EstadoVacio("Sin contratos", "Crea un contrato para ocupar una habitación disponible.")
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(11.dp),
                contentPadding = PaddingValues(bottom = 83.dp)) {
                items(visibles, key = { it.id }) { contrato ->
                    val codigo = habitaciones.find { it.id == contrato.habitacionId }?.codigo ?: "?"
                    val nombre = inquilinos.find { it.id == contrato.inquilinoId }?.nombres ?: "Inquilino"
                    TarjetaRenta(onClick = { onDetalle(contrato.id) }) {
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(nombre, style = MaterialTheme.typography.titleMedium)
                                TextoSecundario("Habitación $codigo")
                            }
                            EtiquetaEstado(if (contrato.fechaFinReal == null) "Vigente" else "Finalizado",
                                positivo = contrato.fechaFinReal == null)
                        }
                        HorizontalDivider(color = BordeRenta)
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                TextoSecundario("Renta mensual")
                                Text(DineroUtils.mostrar(contrato.rentaMensualCent),
                                    style = MaterialTheme.typography.titleSmall, color = VerdeOscuro)
                            }
                            Column {
                                TextoSecundario("Fecha de inicio")
                                Text(contrato.fechaInicio, style = MaterialTheme.typography.titleSmall)
                            }
                        }
                        TextoSecundario("Fin pactado: ${contrato.fechaFinPactada ?: "Sin fecha pactada"}")
                    }
                }
            }
        }
        FloatingActionButton(onClick = onCrear,
            modifier = Modifier.align(Alignment.BottomEnd).padding(22.dp),
            shape = RoundedCornerShape(17.dp), containerColor = VerdeRenta,
            contentColor = Color.White) {
            Icon(Icons.Default.Add, contentDescription = "Registrar contrato")
        }
    }
}
