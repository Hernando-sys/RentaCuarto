package com.rentacuarto.ui.propiedades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.rentacuarto.data.local.entity.HabitacionEntity
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.theme.VerdeRenta

/** P05. Detalle de la propiedad, sus habitaciones y disponibilidad real. */
@Composable
fun PropiedadDetalleScreen(
    propiedadId: Int,
    propiedadesViewModel: PropiedadesViewModel,
    habitacionesViewModel: HabitacionesViewModel,
    onAgregarHabitacion: () -> Unit,
    onEditarHabitacion: (Int) -> Unit,
    onCrearContrato: (Int) -> Unit
) {
    val flujo = remember(propiedadId) { propiedadesViewModel.listarPorId(propiedadId) }
    val propiedades by flujo.collectAsState(initial = emptyList())
    val flujoHabitaciones = remember(propiedadId) { habitacionesViewModel.listarPorPropiedad(propiedadId) }
    val habitaciones by flujoHabitaciones.collectAsState(initial = emptyList())
    val ocupadas by habitacionesViewModel.idsOcupadas.collectAsState()
    val propiedad = propiedades.firstOrNull()
    val ocupadasCount = habitaciones.count { it.id in ocupadas }
    var seleccionada by remember { mutableIntStateOf(0) }
    var paraEliminar by remember { mutableStateOf<HabitacionEntity?>(null) }
    var mensaje by remember { mutableStateOf("") }
    val disponibleElegida = habitaciones.find { it.id == seleccionada && it.id !in ocupadas }

    // Selecciona la primera habitación disponible al abrir el detalle.
    LaunchedEffect(habitaciones, ocupadas) {
        if (habitaciones.none { it.id == seleccionada && it.id !in ocupadas }) {
            seleccionada = habitaciones.firstOrNull { it.id !in ocupadas }?.id ?: 0
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        EncabezadoContenido("Habitaciones", "Gestiona la ocupación y disponibilidad")
        if (propiedad == null) {
            TextoSecundario("Cargando propiedad...")
        } else {
            TarjetaRenta {
                TextoSecundario(propiedad.direccion)
                Spacer(Modifier.height(8.dp))
                ResumenOcupacion(habitaciones.size, ocupadasCount, habitaciones.size - ocupadasCount)
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                TituloSeccion("Habitaciones")
                TextButton(onClick = onAgregarHabitacion) { Text("+ Agregar", color = VerdeRenta) }
            }
            if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(habitaciones, key = { it.id }) { habitacion ->
                    val ocupada = habitacion.id in ocupadas
                    TarjetaRenta(onClick = { seleccionada = habitacion.id }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Hab. ${habitacion.codigo}", style = MaterialTheme.typography.titleMedium)
                                TextoSecundario(habitacion.descripcion.ifEmpty {
                                    if (ocupada) "Con contrato vigente" else "Sin contrato vigente"
                                })
                            }
                            EtiquetaEstado(if (ocupada) "Ocupada" else "Disponible", positivo = !ocupada)
                        }
                        if (seleccionada == habitacion.id) {
                            Row {
                                TextButton(onClick = { onEditarHabitacion(habitacion.id) }) { Text("Editar") }
                                TextButton(onClick = { paraEliminar = habitacion }) { Text("Eliminar") }
                            }
                        }
                    }
                }
            }
            BotonPrincipal("Registrar contrato${disponibleElegida?.let { " en Hab. ${it.codigo}" } ?: ""}",
                onClick = { disponibleElegida?.let { onCrearContrato(it.id) } },
                modifier = Modifier.fillMaxWidth(), enabled = disponibleElegida != null)
        }
    }
    paraEliminar?.let { habitacion ->
        AlertDialog(onDismissRequest = { paraEliminar = null }, title = { Text("Eliminar habitación") },
            text = { Text("Solo se permite eliminar una habitación sin contratos.") },
            confirmButton = { TextButton(onClick = {
                habitacionesViewModel.eliminar(habitacion) { mensaje = it ?: "Habitación eliminada." }
                paraEliminar = null
            }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { paraEliminar = null }) { Text("Cancelar") } })
    }
}
