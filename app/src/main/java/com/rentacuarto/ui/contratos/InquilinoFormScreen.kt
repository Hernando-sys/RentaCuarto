package com.rentacuarto.ui.contratos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*

/** P08. DNI se guarda como texto de ocho dígitos. */
@Composable
fun InquilinoFormScreen(viewModel: InquilinosViewModel, onVolver: () -> Unit) {
    var nombres by rememberSaveable { mutableStateOf("") }
    var dni by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var mensaje by rememberSaveable { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Datos del inquilino", "Registra información para futuros contratos")
            CampoTexto("Nombres y apellidos", nombres, { nombres = it })
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(Modifier.weight(1f)) { CampoTexto("DNI", dni, { dni = it }, tipoTeclado = KeyboardType.Number) }
                Box(Modifier.weight(1f)) { CampoTexto("Teléfono", telefono, { telefono = it }, tipoTeclado = KeyboardType.Phone) }
            }
            MensajeInformativo("Un inquilino puede tener varios contratos a lo largo del tiempo.")
            if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
        AccionesFormulario(onVolver, {
            viewModel.guardar(0, nombres, dni, telefono) { error ->
                if (error == null) onVolver() else mensaje = error
            }
        })
    }
}
