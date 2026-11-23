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

/** P04. Registrar propiedad; el formulario corresponde al wireframe original. */
@Composable
fun PropiedadFormScreen(viewModel: PropiedadesViewModel, onVolver: () -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var tipo by rememberSaveable { mutableStateOf("CASA") }
    var notas by rememberSaveable { mutableStateOf("") }
    var mensaje by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            EncabezadoContenido("Datos de la propiedad", "Completa la información básica de tu inmueble")
            CampoTexto("Nombre de la propiedad", nombre, { nombre = it })
            CampoTexto("Dirección", direccion, { direccion = it })
            TextoSecundario("Tipo de propiedad")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = tipo == "CASA", onClick = { tipo = "CASA" },
                    label = { Text("Casa") }, modifier = Modifier.weight(1f))
                FilterChip(selected = tipo == "EDIFICIO", onClick = { tipo = "EDIFICIO" },
                    label = { Text("Edificio") }, modifier = Modifier.weight(1f))
            }
            CampoTexto("Notas (opcional)", notas, { notas = it }, lineas = 4)
            if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
        Spacer(Modifier.height(12.dp))
        AccionesFormulario(onVolver, {
            viewModel.guardar(0, nombre, direccion, tipo, notas) { error ->
                if (error == null) onVolver() else mensaje = error
            }
        })
    }
}
