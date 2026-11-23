package com.rentacuarto.ui.contratos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*

/** Al recibir el ID de la ruta, Room devuelve sus datos para editarlos. */
@Composable
fun ActualizarInquilinoScreen(idInquilino: Int, viewModel: InquilinosViewModel, onVolver: () -> Unit) {
    val flujo = remember(idInquilino) { viewModel.listarPorId(idInquilino) }
    val registros by flujo.collectAsState(initial = emptyList())
    val inquilino = registros.firstOrNull()
    var nombres by rememberSaveable(idInquilino) { mutableStateOf("") }
    var dni by rememberSaveable(idInquilino) { mutableStateOf("") }
    var telefono by rememberSaveable(idInquilino) { mutableStateOf("") }
    var cargado by rememberSaveable(idInquilino) { mutableStateOf(false) }
    var mensaje by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(inquilino) {
        if (inquilino != null && !cargado) {
            nombres = inquilino.nombres; dni = inquilino.dni; telefono = inquilino.telefono
            cargado = true
        }
    }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Editar inquilino", "Verifica el DNI y el teléfono antes de guardar")
            if (inquilino == null) TextoSecundario("Buscando inquilino...") else {
                CampoTexto("Nombres y apellidos", nombres, { nombres = it })
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(Modifier.weight(1f)) { CampoTexto("DNI", dni, { dni = it }, tipoTeclado = KeyboardType.Number) }
                    Box(Modifier.weight(1f)) { CampoTexto("Teléfono", telefono, { telefono = it }, tipoTeclado = KeyboardType.Phone) }
                }
                MensajeInformativo("Un inquilino puede tener varios contratos a lo largo del tiempo.")
                if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
            }
        }
        AccionesFormulario(onVolver, {
            viewModel.guardar(idInquilino, nombres, dni, telefono) { error ->
                if (error == null) onVolver() else mensaje = error
            }
        }, "Actualizar", enabled = cargado)
    }
}
