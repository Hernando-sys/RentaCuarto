package com.rentacuarto.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.theme.*

/** Indicador compacto para el panel. Mantiene la firma anterior. */
@Composable
fun TarjetaIndicador(titulo: String, valor: String, detalle: String, modifier: Modifier = Modifier) {
    TarjetaRenta(modifier) {
        Box(Modifier.width(30.dp).height(4.dp).background(VerdeRenta, RoundedCornerShape(4.dp)))
        TextoSecundario(titulo)
        Text(valor, style = MaterialTheme.typography.headlineSmall, color = TextoRenta)
        TextoSecundario(detalle)
    }
}
