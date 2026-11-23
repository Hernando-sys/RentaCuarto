package com.rentacuarto.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.House
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rentacuarto.navigation.Routes
import com.rentacuarto.ui.theme.*

private data class Seccion(val ruta: String, val nombre: String, val icono: ImageVector)

/** Tres accesos principales para el alcance P01-P12. */
@Composable
fun BarraNavegacion(navController: NavHostController, rutaActual: String?) {
    val secciones = listOf(
        Seccion(Routes.Inicio.ruta, "Inicio", Icons.Default.Home),
        Seccion(Routes.Propiedades.ruta, "Propiedades", Icons.Default.House),
        Seccion(Routes.Contratos.ruta, "Contratos", Icons.Default.Assignment)
    )
    Column {
        HorizontalDivider(color = BordeRenta)
        NavigationBar(containerColor = BlancoRenta, tonalElevation = 0.dp) {
            secciones.forEach { seccion ->
                NavigationBarItem(
                    selected = rutaActual == seccion.ruta,
                    onClick = {
                        navController.navigate(seccion.ruta) {
                            popUpTo(Routes.Inicio.ruta) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(seccion.icono, contentDescription = seccion.nombre) },
                    label = { Text(seccion.nombre, style = MaterialTheme.typography.labelSmall) },
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VerdeOscuro,
                        selectedTextColor = VerdeOscuro,
                        unselectedIconColor = GrisTexto,
                        unselectedTextColor = GrisTexto,
                        indicatorColor = VerdeClaro
                    )
                )
            }
        }
    }
}
