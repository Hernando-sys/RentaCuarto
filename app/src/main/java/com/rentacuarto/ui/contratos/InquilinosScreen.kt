package com.rentacuarto.ui.contratos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rentacuarto.data.local.entity.InquilinoEntity
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.theme.*

/** P07. Listado de inquilinos con búsqueda, edición y eliminación controlada. */
@Composable
fun InquilinosScreen(viewModel: InquilinosViewModel, onCrear: () -> Unit,
    onEditar: (Int) -> Unit, onContratos: () -> Unit = {}) {
    val inquilinos by viewModel.inquilinos.collectAsState()
    var buscar by rememberSaveable { mutableStateOf("") }
    var paraEliminar by remember { mutableStateOf<InquilinoEntity?>(null) }
    var mensaje by remember { mutableStateOf("") }
    val lista = inquilinos.filter { it.nombres.contains(buscar, true) || it.dni.contains(buscar) }
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(horizontal = 17.dp, vertical = 17.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)) {
            EncabezadoContenido("Inquilinos", "Personas registradas para tus contratos")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                BotonBorde("Contratos", onContratos, Modifier.weight(1f))
                BotonPrincipal("Inquilinos", {}, Modifier.weight(1f))
            }
            OutlinedTextField(buscar, { buscar = it }, modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar por nombre o DNI") }, singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GrisTexto) },
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = BlancoRenta,
                    unfocusedContainerColor = BlancoRenta,
                    focusedBorderColor = VerdeRenta, unfocusedBorderColor = BordeRenta))
            if (mensaje.isNotBlank()) MensajeInformativo(mensaje)
            TextoSecundario("${lista.size} ${if (lista.size == 1) "inquilino" else "inquilinos"} encontrados")
            if (lista.isEmpty()) EstadoVacio("Sin inquilinos", "Agrega un inquilino para asociarlo a un contrato.")
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(11.dp),
                contentPadding = PaddingValues(bottom = 83.dp)) {
                items(lista, key = { it.id }) { inquilino ->
                    TarjetaRenta(onClick = { onEditar(inquilino.id) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(Modifier.size(48.dp), shape = CircleShape, color = VerdeClaro) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(inquilino.nombres.take(1).uppercase(), color = VerdeOscuro,
                                        style = MaterialTheme.typography.titleMedium)
                                }
                            }
                            Spacer(Modifier.width(11.dp))
                            Column(Modifier.weight(1f)) {
                                Text(inquilino.nombres, style = MaterialTheme.typography.titleSmall)
                                TextoSecundario("DNI ${inquilino.dni}")
                                TextoSecundario(inquilino.telefono.ifBlank { "Teléfono no registrado" })
                            }
                            IconButton(onClick = { onEditar(inquilino.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar inquilino", tint = VerdeOscuro)
                            }
                            IconButton(onClick = { paraEliminar = inquilino }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar inquilino", tint = GrisTexto)
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(onClick = onCrear,
            modifier = Modifier.align(Alignment.BottomEnd).padding(22.dp),
            shape = RoundedCornerShape(17.dp), containerColor = VerdeRenta, contentColor = Color.White) {
            Icon(Icons.Default.Add, contentDescription = "Nuevo inquilino")
        }
    }
    paraEliminar?.let { item ->
        AlertDialog(onDismissRequest = { paraEliminar = null }, title = { Text("Eliminar inquilino") },
            text = { Text("Solo puedes eliminarlo si no tiene contratos.") },
            confirmButton = { TextButton(onClick = {
                viewModel.eliminar(item) { mensaje = it ?: "Inquilino eliminado." }
                paraEliminar = null
            }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { paraEliminar = null }) { Text("Cancelar") } })
    }
}
