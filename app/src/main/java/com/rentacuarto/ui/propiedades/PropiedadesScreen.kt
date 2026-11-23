package com.rentacuarto.ui.propiedades

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rentacuarto.data.local.entity.PropiedadEntity
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.theme.*

/** P03. Una tarjeta por propiedad con los datos reales de sus habitaciones. */
@Composable
fun PropiedadesScreen(
    viewModel: PropiedadesViewModel,
    onCrear: () -> Unit,
    onDetalle: (Int) -> Unit,
    onEditar: (Int) -> Unit,
    habitacionesViewModel: HabitacionesViewModel
) {
    val propiedades by viewModel.propiedades.collectAsState()
    val habitaciones by habitacionesViewModel.habitaciones.collectAsState()
    val ocupadas by habitacionesViewModel.idsOcupadas.collectAsState()
    var busqueda by rememberSaveable { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    var paraEliminar by remember { mutableStateOf<PropiedadEntity?>(null) }
    val filtradas = propiedades.filter { it.nombre.contains(busqueda, ignoreCase = true) }
    Box(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(horizontal = 17.dp, vertical = 17.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp)) {
            EncabezadoContenido("Mis propiedades", "Organiza tus inmuebles y habitaciones")
            OutlinedTextField(busqueda, { busqueda = it },
                placeholder = { Text("Buscar por nombre") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = GrisTexto) },
                singleLine = true,
                shape = RoundedCornerShape(13.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = BlancoRenta, unfocusedContainerColor = BlancoRenta,
                    focusedBorderColor = VerdeRenta, unfocusedBorderColor = BordeRenta),
                modifier = Modifier.fillMaxWidth())
            if (mensaje.isNotBlank()) MensajeInformativo(mensaje)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                TextoSecundario("${filtradas.size} ${if (filtradas.size == 1) "propiedad" else "propiedades"}")
                TextButton(onClick = onCrear) { Text("+ Nueva propiedad") }
            }
            if (filtradas.isEmpty()) EstadoVacio("No hay propiedades",
                if (busqueda.isEmpty()) "Presiona Nueva propiedad para registrar la primera."
                else "Prueba con otro nombre de búsqueda.")
            LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(13.dp),
                contentPadding = PaddingValues(bottom = 83.dp)) {
                items(filtradas, key = { it.id }) { propiedad ->
                    val cuartos = habitaciones.filter { it.propiedadId == propiedad.id }
                    val ocupados = cuartos.count { it.id in ocupadas }
                    TarjetaRenta(onClick = { onDetalle(propiedad.id) }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = VerdeClaro, shape = RoundedCornerShape(13.dp),
                                modifier = Modifier.size(52.dp)) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.House, contentDescription = null,
                                        tint = VerdeOscuro, modifier = Modifier.size(26.dp))
                                }
                            }
                            Spacer(Modifier.width(11.dp))
                            Column(Modifier.weight(1f)) {
                                Text(propiedad.nombre, style = MaterialTheme.typography.titleMedium)
                                TextoSecundario(propiedad.direccion)
                            }
                            IconButton(onClick = { onEditar(propiedad.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar propiedad", tint = VerdeOscuro)
                            }
                            IconButton(onClick = { paraEliminar = propiedad }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar propiedad", tint = GrisTexto)
                            }
                        }
                        HorizontalDivider(color = BordeRenta)
                        ResumenOcupacion(cuartos.size, ocupados, cuartos.size - ocupados)
                        Text("Toca la tarjeta para ver las habitaciones",
                            style = MaterialTheme.typography.labelSmall, color = VerdeOscuro)
                    }
                }
            }
        }
        FloatingActionButton(onClick = onCrear,
            modifier = Modifier.align(Alignment.BottomEnd).padding(22.dp),
            shape = RoundedCornerShape(17.dp), containerColor = VerdeRenta,
            contentColor = Color.White) {
            Icon(Icons.Default.Add, contentDescription = "Registrar propiedad")
        }
    }
    paraEliminar?.let { propiedad ->
        AlertDialog(onDismissRequest = { paraEliminar = null },
            title = { Text("Eliminar propiedad") },
            text = { Text("¿Eliminar ${propiedad.nombre}? Solo se permite si no tiene habitaciones ni gastos.") },
            confirmButton = { TextButton(onClick = {
                viewModel.eliminar(propiedad) { mensaje = it ?: "Propiedad eliminada." }
                paraEliminar = null
            }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { paraEliminar = null }) { Text("Cancelar") } })
    }
}
