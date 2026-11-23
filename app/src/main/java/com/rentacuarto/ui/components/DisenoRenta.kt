package com.rentacuarto.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.theme.*

/** Componentes comunes. Todos usan Material 3, igual que los ejemplos del curso. */
@Composable
fun TarjetaRenta(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contenido: @Composable ColumnScope.() -> Unit
) {
    val forma = RoundedCornerShape(17.dp)
    val modificador = if (onClick == null) modifier else modifier.clip(forma).clickable(onClick = onClick)
    Card(
        modifier = modificador,
        shape = forma,
        border = BorderStroke(1.dp, BordeRenta),
        colors = CardDefaults.cardColors(containerColor = BlancoRenta),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp), content = contenido)
    }
}

@Composable
fun TituloSeccion(titulo: String, modifier: Modifier = Modifier) {
    Text(titulo, modifier = modifier, style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold, color = TextoRenta)
}

@Composable
fun TextoSecundario(texto: String, modifier: Modifier = Modifier) {
    Text(texto, modifier = modifier, style = MaterialTheme.typography.bodySmall,
        color = GrisTexto)
}

/** Estado con color e indicador circular: no depende solo del color para identificarse. */
@Composable
fun EtiquetaEstado(texto: String, positivo: Boolean = false, alerta: Boolean = false) {
    val fondo = if (alerta) RojoClaro else if (positivo) VerdeClaro else AzulClaro
    val letra = if (alerta) RojoRenta else if (positivo) VerdeOscuro else AzulTexto
    Surface(color = fondo, shape = RoundedCornerShape(24.dp)) {
        Row(Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(6.dp).background(letra, CircleShape))
            Text(texto.uppercase(), style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold, color = letra, maxLines = 1)
        }
    }
}

@Composable
fun BotonPrincipal(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Button(onClick = onClick, enabled = enabled,
        modifier = modifier.heightIn(min = 49.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 13.dp, vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = VerdeRenta,
            contentColor = BlancoRenta, disabledContainerColor = BordeRenta)) {
        Text(texto, style = MaterialTheme.typography.labelLarge, maxLines = 2)
    }
}

@Composable
fun BotonBorde(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier, peligro: Boolean = false) {
    val color = if (peligro) RojoRenta else VerdeRenta
    OutlinedButton(onClick = onClick, modifier = modifier.heightIn(min = 49.dp),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 11.dp, vertical = 8.dp),
        border = BorderStroke(1.dp, color),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = BlancoRenta)) {
        Text(texto, color = color, style = MaterialTheme.typography.labelLarge, maxLines = 2)
    }
}

@Composable
fun AccionesFormulario(
    onCancelar: () -> Unit,
    onGuardar: () -> Unit,
    texto: String = "Guardar",
    enabled: Boolean = true
) {
    // Fila inferior compartida por los formularios del proyecto.
    Surface(shape = RoundedCornerShape(16.dp), color = BlancoRenta,
        border = BorderStroke(1.dp, BordeRenta), shadowElevation = 1.dp) {
        Row(Modifier.fillMaxWidth().padding(9.dp), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            BotonBorde("Cancelar", onCancelar, Modifier.weight(1f))
            BotonPrincipal(texto, onGuardar, Modifier.weight(1f), enabled)
        }
    }
}

@Composable
fun MensajeInformativo(texto: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = VerdeClaro,
        shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Info, contentDescription = null, tint = VerdeOscuro,
                modifier = Modifier.size(18.dp))
            Text(texto, style = MaterialTheme.typography.bodySmall, color = VerdeOscuro)
        }
    }
}

@Composable
fun MensajeAdvertencia(texto: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = AmarilloClaro,
        shape = RoundedCornerShape(12.dp)) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Info, contentDescription = null, tint = AmarilloTexto,
                modifier = Modifier.size(18.dp))
            Text(texto, style = MaterialTheme.typography.bodySmall, color = AmarilloTexto)
        }
    }
}

@Composable
fun DatoGrande(numero: String, etiqueta: String, modifier: Modifier = Modifier, color: Color = TextoRenta) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(numero, style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold, color = color, maxLines = 1,
            overflow = TextOverflow.Ellipsis)
        TextoSecundario(etiqueta)
    }
}

@Composable
fun ResumenOcupacion(total: Int, ocupadas: Int, disponibles: Int, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            DatoGrande(total.toString(), "Habitaciones")
            DatoGrande(ocupadas.toString(), "Ocupadas")
            DatoGrande(disponibles.toString(), "Disponibles", color = VerdeRenta)
        }
        LinearProgressIndicator(
            progress = { if (total == 0) 0f else (ocupadas.toFloat() / total).coerceIn(0f, 1f) },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(8.dp)),
            color = VerdeRenta, trackColor = Color(0xFFE7EDF0)
        )
    }
}

@Composable
fun SelectorSimple(etiqueta: String, valor: String, opciones: List<String>, alElegir: (String) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (etiqueta.isNotBlank()) {
            Text(etiqueta, style = MaterialTheme.typography.labelMedium,
                color = GrisTexto)
        }
        Box {
            OutlinedButton(onClick = { expandido = true },
                modifier = Modifier.fillMaxWidth().heightIn(min = 50.dp),
                border = BorderStroke(1.dp, BordeRenta),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = BlancoRenta),
                contentPadding = PaddingValues(horizontal = 13.dp)) {
                Text(valor.ifEmpty { "Seleccionar" }, color = TextoRenta,
                    modifier = Modifier.weight(1f), maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                    overflow = TextOverflow.Ellipsis)
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Opciones", tint = GrisTexto)
            }
            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(text = { Text(opcion) }, onClick = {
                        alElegir(opcion)
                        expandido = false
                    })
                }
            }
        }
    }
}

/** Cabecera local para pantallas y formularios que necesitan contexto adicional. */
@Composable
fun EncabezadoContenido(titulo: String, descripcion: String, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleLarge, color = TextoRenta)
        TextoSecundario(descripcion)
    }
}

/** Estado vacío reutilizable en listas, sin información inventada. */
@Composable
fun EstadoVacio(titulo: String, descripcion: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxWidth(), color = BlancoRenta,
        border = BorderStroke(1.dp, BordeRenta), shape = RoundedCornerShape(17.dp)) {
        Column(Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Default.Search, contentDescription = null, tint = VerdeRenta,
                modifier = Modifier.size(32.dp))
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = TextoRenta)
            Text(descripcion, style = MaterialTheme.typography.bodySmall,
                color = GrisTexto, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}
