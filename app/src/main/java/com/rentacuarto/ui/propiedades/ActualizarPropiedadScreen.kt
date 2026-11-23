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

/** Editar datos: Flow por ID y LaunchedEffect, como en Proyecto Funcional 3. */
@Composable
fun ActualizarPropiedadScreen(idPropiedad: Int, viewModel: PropiedadesViewModel, onVolver: () -> Unit) {
    val flujo = remember(idPropiedad) { viewModel.listarPorId(idPropiedad) }
    val registros by flujo.collectAsState(initial = emptyList())
    val propiedad = registros.firstOrNull()
    var nombre by rememberSaveable(idPropiedad) { mutableStateOf("") }
    var direccion by rememberSaveable(idPropiedad) { mutableStateOf("") }
    var tipo by rememberSaveable(idPropiedad) { mutableStateOf("CASA") }
    var notas by rememberSaveable(idPropiedad) { mutableStateOf("") }
    var cargado by rememberSaveable(idPropiedad) { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(propiedad) {
        if (propiedad != null && !cargado) {
            nombre = propiedad.nombre; direccion = propiedad.direccion
            tipo = propiedad.tipo; notas = propiedad.notas; cargado = true
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Editar datos", "Los cambios se guardan en tu dispositivo")
            if (propiedad == null) TextoSecundario("Buscando propiedad...") else {
                CampoTexto("Nombre de la propiedad", nombre, { nombre = it })
                CampoTexto("Dirección", direccion, { direccion = it })
                TextoSecundario("Tipo de propiedad")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(tipo == "CASA", onClick = { tipo = "CASA" },
                        label = { Text("Casa") }, modifier = Modifier.weight(1f))
                    FilterChip(tipo == "EDIFICIO", onClick = { tipo = "EDIFICIO" },
                        label = { Text("Edificio") }, modifier = Modifier.weight(1f))
                }
                CampoTexto("Notas (opcional)", notas, { notas = it }, lineas = 4)
                if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
        AccionesFormulario(onVolver, {
            viewModel.guardar(idPropiedad, nombre, direccion, tipo, notas) { error ->
                if (error == null) onVolver() else mensaje = error
            }
        }, "Actualizar", enabled = cargado)
    }
}
