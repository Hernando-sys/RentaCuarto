package com.rentacuarto.ui.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.contratos.ContratosViewModel
import com.rentacuarto.ui.contratos.InquilinosViewModel
import java.time.LocalDate

/** P02: alertas de finalización próxima, sin el módulo de cobranza. */
@Composable
fun AlertasScreen(contratosVM: ContratosViewModel,
    inquilinosVM: InquilinosViewModel, onContrato: (Int) -> Unit) {
    val contratos by contratosVM.contratos.collectAsState()
    val inquilinos by inquilinosVM.inquilinos.collectAsState()
    val hoy = LocalDate.now()
    val proximos = contratos.filter { contrato ->
        contrato.fechaFinReal == null && contrato.fechaInicio <= hoy.toString() &&
            contrato.fechaFinPactada?.let { fecha ->
                runCatching { LocalDate.parse(fecha) }.getOrNull()?.let {
                    !it.isBefore(hoy) && !it.isAfter(hoy.plusDays(30))
                }
            } == true
    }.sortedBy { it.fechaFinPactada }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement=Arrangement.spacedBy(12.dp)) {
        EncabezadoContenido("Alertas de contratos", "Próximos a finalizar en los siguientes 30 días")
        TextoSecundario("Contratos encontrados: ${proximos.size}")
        if (proximos.isEmpty())
            EstadoVacio("Sin contratos próximos a finalizar",
                "La sección mostrará los contratos con una fecha pactada dentro de 30 días.")
        proximos.forEach { contrato ->
            val nombre = inquilinos.find { it.id==contrato.inquilinoId }?.nombres ?: "Inquilino"
            TarjetaRenta(onClick={onContrato(contrato.id)}) {
                TituloSeccion(nombre)
                TextoSecundario("Contrato #${contrato.id}")
                TextoSecundario("Fin pactado: ${contrato.fechaFinPactada}")
                EtiquetaEstado("Próximo a finalizar", alerta=true)
                TextButton(onClick={onContrato(contrato.id)}) { Text("Ver contrato") }
            }
        }
    }
}
