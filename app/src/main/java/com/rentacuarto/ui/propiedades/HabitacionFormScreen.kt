package com.rentacuarto.ui.propiedades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*

/** P06. El estado ocupado/disponible no se introduce manualmente. */
@Composable
fun HabitacionFormScreen(
    propiedadId: Int,
    viewModel: HabitacionesViewModel,
    onVolver: () -> Unit,
    propiedadesViewModel: PropiedadesViewModel
) {
    var codigo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var mensaje by rememberSaveable { mutableStateOf("") }
    val propiedades by propiedadesViewModel.propiedades.collectAsState()
    val nombrePropiedad = propiedades.find { it.id == propiedadId }?.nombre ?: "Propiedad $propiedadId"

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Nueva habitación", "Asocia esta habitación a una propiedad")
            TextoSecundario("Propiedad")
            TarjetaRenta { Text(nombrePropiedad) }
            CampoTexto("Número o nombre", codigo, { codigo = it })
            CampoTexto("Descripción (opcional)", descripcion, { descripcion = it }, lineas = 4)
            MensajeInformativo("El estado (ocupada o disponible) se calcula según el contrato vigente.")
            if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(12.dp))
        AccionesFormulario(onVolver, {
            viewModel.guardar(0, propiedadId, codigo, descripcion) { error ->
                if (error == null) onVolver() else mensaje = error
            }
        })
    }
}
