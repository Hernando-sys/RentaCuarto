package com.rentacuarto.ui.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rentacuarto.ui.components.*
import com.rentacuarto.ui.contratos.ContratosViewModel
import com.rentacuarto.ui.propiedades.HabitacionesViewModel
import com.rentacuarto.ui.propiedades.PropiedadesViewModel
import com.rentacuarto.ui.theme.*
import com.rentacuarto.util.FechaUtils
import java.time.LocalDate

/** P01: información real de ocupación, sin cifras de cobros o gastos. */
@Composable
fun InicioScreen(
    propiedadesVM: PropiedadesViewModel,
    habitacionesVM: HabitacionesViewModel,
    contratosVM: ContratosViewModel,
    onVerPropiedades: () -> Unit,
    onVerContratos: () -> Unit,
    onVerAlertas: () -> Unit
) {
    val propiedades by propiedadesVM.propiedades.collectAsState()
    val habitaciones by habitacionesVM.habitaciones.collectAsState()
    val ocupadasIds by habitacionesVM.idsOcupadas.collectAsState()
    val contratos by contratosVM.contratos.collectAsState()
    var nombrePropiedad by remember { mutableStateOf("Todas las propiedades") }
    val idPropiedad = propiedades.find { it.nombre == nombrePropiedad }?.id
    val visibles = habitaciones.filter { idPropiedad == null || it.propiedadId == idPropiedad }
    val ocupadas = visibles.count { it.id in ocupadasIds }
    val libres = visibles.size - ocupadas
    val vigentes = contratos.count { it.fechaFinReal == null && it.fechaInicio <= FechaUtils.hoy() }
    val total = visibles.size

    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        .padding(horizontal=17.dp, vertical=18.dp),
        verticalArrangement=Arrangement.spacedBy(17.dp)) {
        Row(verticalAlignment=Alignment.CenterVertically) {
            Surface(color=VerdeClaro, shape=RoundedCornerShape(14.dp)) {
                Icon(Icons.Default.Home, contentDescription=null, tint=VerdeOscuro,
                    modifier=Modifier.padding(12.dp).size(26.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("RentaCuarto", style=MaterialTheme.typography.titleLarge,
                    color=TextoRenta)
                TextoSecundario("Gestión de propiedades y habitaciones")
            }
            IconButton(onClick=onVerAlertas) {
                Icon(Icons.Default.Notifications, contentDescription="Ver alertas", tint=VerdeOscuro)
            }
        }
        Surface(shape=RoundedCornerShape(22.dp), color=VerdeOscuro) {
            Column(Modifier.fillMaxWidth().background(
                Brush.linearGradient(listOf(VerdeRenta,VerdeOscuro)))
                .padding(19.dp), verticalArrangement=Arrangement.spacedBy(12.dp)) {
                Text("RESUMEN DE OCUPACIÓN", style=MaterialTheme.typography.labelMedium,
                    color=Color(0xFFCAEAE3))
                Text("$ocupadas de $total habitaciones", style=MaterialTheme.typography.headlineMedium,
                    color=Color.White)
                LinearProgressIndicator(
                    progress={if(total==0) 0f else ocupadas.toFloat()/total},
                    modifier=Modifier.fillMaxWidth().height(8.dp),
                    color=Color(0xFFAAE9D3), trackColor=Color(0x55FFFFFF))
                Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween) {
                    Text("$libres disponibles",color=Color.White)
                    Text("${if(total==0) "0.0" else String.format(java.util.Locale.US, "%.1f",100.0*ocupadas/total)} % ocupación",color=Color.White)
                }
            }
        }
        SelectorSimple("MOSTRAR PROPIEDAD",nombrePropiedad,
            listOf("Todas las propiedades")+propiedades.map { it.nombre }) { nombrePropiedad=it }
        Row(horizontalArrangement=Arrangement.spacedBy(10.dp)) {
            TarjetaIndicador("Propiedades", propiedades.size.toString(),
                "Registradas", Modifier.weight(1f))
            TarjetaIndicador("Contratos", vigentes.toString(),
                "Vigentes hoy", Modifier.weight(1f))
        }
        TituloSeccion("Accesos rápidos")
        Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.spacedBy(9.dp)) {
            BotonPrincipal("Propiedades",onVerPropiedades,Modifier.weight(1f))
            BotonBorde("Contratos",onVerContratos,Modifier.weight(1f))
        }
        TituloSeccion("Mis propiedades")
        if (propiedades.isEmpty())
            EstadoVacio("Aún no hay propiedades", "Registra una propiedad para comenzar.")
        propiedades.filter { idPropiedad==null || it.id==idPropiedad }.forEach { propiedad ->
            val rooms=habitaciones.filter { it.propiedadId==propiedad.id }
            val n=rooms.count { it.id in ocupadasIds }
            TarjetaRenta(onClick=onVerPropiedades) {
                TituloSeccion(propiedad.nombre)
                TextoSecundario(propiedad.direccion)
                Spacer(Modifier.height(7.dp))
                ResumenOcupacion(rooms.size,n,rooms.size-n)
            }
        }
        TextoSecundario("Los indicadores se calculan con los contratos de la base local Room.")
    }
}
