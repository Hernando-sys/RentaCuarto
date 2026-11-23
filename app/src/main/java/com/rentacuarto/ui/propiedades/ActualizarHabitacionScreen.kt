package com.rentacuarto.ui.propiedades

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*

/** Actualiza una habitación y preserva su relación con la propiedad. */
@Composable
fun ActualizarHabitacionScreen(idHabitacion: Int, viewModel: HabitacionesViewModel, onVolver: () -> Unit) {
    val flujo = remember(idHabitacion) { viewModel.listarPorId(idHabitacion) }
    val registros by flujo.collectAsState(initial = emptyList())
    val habitacion = registros.firstOrNull()
    var codigo by rememberSaveable(idHabitacion) { mutableStateOf("") }
    var descripcion by rememberSaveable(idHabitacion) { mutableStateOf("") }
    var cargado by rememberSaveable(idHabitacion) { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(habitacion) {
        if (habitacion != null && !cargado) {
            codigo = habitacion.codigo; descripcion = habitacion.descripcion; cargado = true
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Editar habitación", "Actualiza sus datos sin cambiar su ocupación")
            if (habitacion == null) TextoSecundario("Buscando habitación...") else {
                TarjetaRenta { TextoSecundario("Propiedad ID: ${habitacion.propiedadId}") }
                CampoTexto("Número o nombre", codigo, { codigo = it })
                CampoTexto("Descripción (opcional)", descripcion, { descripcion = it }, lineas = 4)
                MensajeInformativo("La ocupación se calcula usando los contratos vigentes.")
                if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
        AccionesFormulario(onVolver, {
            habitacion?.let {
                viewModel.guardar(idHabitacion, it.propiedadId, codigo, descripcion) { error ->
                    if (error == null) onVolver() else mensaje = error
                }
            }
        }, "Actualizar", enabled = cargado)
    }
}
