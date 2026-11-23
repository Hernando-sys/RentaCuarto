package com.rentacuarto.ui.contratos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*
import com.rentacuarto.util.FechaUtils

/** P12. Una fecha de fin pactada NO cierra el contrato. */
@Composable
fun FinalizarContratoScreen(idContrato: Int, viewModel: ContratosViewModel, onVolver: () -> Unit) {
    val flujo = remember(idContrato) { viewModel.listarPorId(idContrato) }
    val contratos by flujo.collectAsState(initial = emptyList())
    val contrato = contratos.firstOrNull()
    var fecha by rememberSaveable { mutableStateOf(FechaUtils.hoy()) }
    var confirmado by rememberSaveable { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            EncabezadoContenido("Cerrar contrato", "El historial se conserva después de finalizar")
            TarjetaRenta {
                TituloSeccion("Contrato #$idContrato")
                TextoSecundario("Inicio: ${contrato?.fechaInicio ?: "..."}")
                TextoSecundario("Fin pactado: ${contrato?.fechaFinPactada ?: "Sin fecha"}")
            }
            MensajeAdvertencia("El historial se conserva. La habitación queda disponible para nuevos contratos al registrar el fin real.")
            CampoTexto("Fecha de fin real (AAAA-MM-DD)", fecha, { fecha = it })
            Row { Checkbox(checked = confirmado, onCheckedChange = { confirmado = it })
                Text("Confirmo que deseo finalizar este contrato", Modifier.padding(top = 13.dp)) }
            if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.error)
        }
        AccionesFormulario(onVolver, { viewModel.finalizar(idContrato, fecha) { error ->
            if (error == null) onVolver() else mensaje = error
        } }, "Finalizar", enabled = confirmado && contrato?.fechaFinReal == null)
    }
}
