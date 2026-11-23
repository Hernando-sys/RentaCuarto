package com.rentacuarto.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.rentacuarto.data.local.RentaCuartoDatabase
import com.rentacuarto.data.repository.*
import com.rentacuarto.ui.components.BarraNavegacion
import com.rentacuarto.ui.contratos.*
import com.rentacuarto.ui.inicio.*
import com.rentacuarto.ui.propiedades.*
import com.rentacuarto.ui.theme.*

/** Solo tres repositorios: no hay módulos de pagos ni gastos en esta entrega. */
private class FabricaViewModels(private val db: RentaCuartoDatabase): ViewModelProvider.Factory {
    private val propiedades = PropiedadesRepository(db)
    private val inquilinos = InquilinosRepository(db)
    private val contratos = ContratosRepository(db)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        PropiedadesViewModel::class.java -> PropiedadesViewModel(propiedades) as T
        HabitacionesViewModel::class.java -> HabitacionesViewModel(propiedades) as T
        InquilinosViewModel::class.java -> InquilinosViewModel(inquilinos) as T
        ContratosViewModel::class.java -> ContratosViewModel(contratos) as T
        else -> error("ViewModel no reconocido: ${modelClass.simpleName}")
    }
}

/** NavHost de la exposición: P01-P12 y formularios de edición. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val contexto = LocalContext.current.applicationContext
    val fabrica = remember(contexto) { FabricaViewModels(RentaCuartoDatabase.obtener(contexto)) }
    val propiedadesVM: PropiedadesViewModel = viewModel(factory = fabrica)
    val habitacionesVM: HabitacionesViewModel = viewModel(factory = fabrica)
    val inquilinosVM: InquilinosViewModel = viewModel(factory = fabrica)
    val contratosVM: ContratosViewModel = viewModel(factory = fabrica)

    val entrada by navController.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route ?: Routes.Inicio.ruta
    val principales = setOf(Routes.Inicio.ruta, Routes.Propiedades.ruta, Routes.Contratos.ruta)
    val esPrincipal = ruta in principales
    val propiedades by propiedadesVM.propiedades.collectAsState()

    val titulo = when (ruta) {
        Routes.Inicio.ruta -> "Inicio"
        Routes.Alertas.ruta -> "Alertas"
        Routes.Propiedades.ruta -> "Propiedades"
        Routes.Contratos.ruta -> "Contratos"
        Routes.Inquilinos.ruta -> "Inquilinos"
        Routes.CrearPropiedad.ruta -> "Registrar propiedad"
        Routes.CrearInquilino.ruta -> "Registrar inquilino"
        else -> when {
            ruta.startsWith("detallePropiedad/") -> {
                val id = entrada?.arguments?.getInt("idPropiedad")
                propiedades.find { it.id == id }?.nombre ?: "Detalle de propiedad"
            }
            ruta.startsWith("actualizarPropiedad/") -> "Editar propiedad"
            ruta.startsWith("crearHabitacion/") -> "Registrar habitación"
            ruta.startsWith("actualizarHabitacion/") -> "Editar habitación"
            ruta.startsWith("actualizarInquilino/") -> "Editar inquilino"
            ruta.startsWith("crearContrato/") -> "Registrar contrato"
            ruta.startsWith("detalleContrato/") -> "Detalle de contrato"
            ruta.startsWith("finalizarContrato/") -> "Finalizar contrato"
            else -> "RentaCuarto EXPO"
        }
    }
    fun irInicio() {
        navController.navigate(Routes.Inicio.ruta) {
            popUpTo(Routes.Inicio.ruta) { inclusive = false }
            launchSingleTop = true
        }
    }
    Scaffold(
        topBar = {
            if (ruta != Routes.Inicio.ruta) {
                TopAppBar(
                    title = { Column {
                        Text(titulo, style=MaterialTheme.typography.titleMedium, color=TextoRenta)
                        if (esPrincipal) Text("Gestión de alquileres · Exposición",
                            style=MaterialTheme.typography.bodySmall, color=GrisTexto)
                    } },
                    navigationIcon = { if (!esPrincipal) IconButton(onClick = {
                        if (!navController.popBackStack()) irInicio()
                    }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription="Volver") } },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor=BlancoRenta)
                )
            }
        },
        bottomBar = { if (esPrincipal) BarraNavegacion(navController, ruta) },
        containerColor = FondoRenta
    ) { margen ->
        NavHost(navController, startDestination=Routes.Inicio.ruta,
            modifier=Modifier.padding(margen)) {
            composable(Routes.Inicio.ruta) { InicioScreen(propiedadesVM, habitacionesVM,
                contratosVM, onVerPropiedades = { navController.navigate(Routes.Propiedades.ruta) },
                onVerContratos = { navController.navigate(Routes.Contratos.ruta) },
                onVerAlertas = { navController.navigate(Routes.Alertas.ruta) }) }
            composable(Routes.Alertas.ruta) { AlertasScreen(contratosVM, inquilinosVM,
                onContrato = { navController.navigate(Routes.DetalleContrato.crearRuta(it)) }) }
            composable(Routes.Propiedades.ruta) { PropiedadesScreen(propiedadesVM,
                onCrear = { navController.navigate(Routes.CrearPropiedad.ruta) },
                onDetalle = { navController.navigate(Routes.DetallePropiedad.crearRuta(it)) },
                onEditar = { navController.navigate(Routes.ActualizarPropiedad.crearRuta(it)) },
                habitacionesViewModel = habitacionesVM) }
            composable(Routes.CrearPropiedad.ruta) {
                PropiedadFormScreen(propiedadesVM) { navController.popBackStack() }
            }
            composable(Routes.DetallePropiedad.ruta,
                arguments = listOf(navArgument("idPropiedad") { type = NavType.IntType })) { item ->
                val id = item.arguments?.getInt("idPropiedad") ?: 0
                PropiedadDetalleScreen(id, propiedadesVM, habitacionesVM,
                    onAgregarHabitacion = { navController.navigate(Routes.CrearHabitacion.crearRuta(id)) },
                    onEditarHabitacion = { navController.navigate(Routes.ActualizarHabitacion.crearRuta(it)) },
                    onCrearContrato = { navController.navigate(Routes.CrearContrato.crearRuta(it)) })
            }
            composable(Routes.ActualizarPropiedad.ruta,
                arguments = listOf(navArgument("idPropiedad") { type = NavType.IntType })) { item ->
                ActualizarPropiedadScreen(item.arguments?.getInt("idPropiedad") ?: 0, propiedadesVM) {
                    navController.popBackStack()
                }
            }
            composable(Routes.CrearHabitacion.ruta,
                arguments = listOf(navArgument("idPropiedad") { type = NavType.IntType })) { item ->
                HabitacionFormScreen(item.arguments?.getInt("idPropiedad") ?: 0, habitacionesVM,
                    onVolver = { navController.popBackStack() }, propiedadesViewModel = propiedadesVM)
            }
            composable(Routes.ActualizarHabitacion.ruta,
                arguments = listOf(navArgument("idHabitacion") { type = NavType.IntType })) { item ->
                ActualizarHabitacionScreen(item.arguments?.getInt("idHabitacion") ?: 0, habitacionesVM) {
                    navController.popBackStack()
                }
            }
            composable(Routes.Contratos.ruta) { ContratosScreen(contratosVM, habitacionesVM,
                inquilinosVM, onCrear = { navController.navigate(Routes.CrearContrato.crearRuta()) },
                onInquilinos = { navController.navigate(Routes.Inquilinos.ruta) },
                onDetalle = { navController.navigate(Routes.DetalleContrato.crearRuta(it)) }) }
            composable(Routes.Inquilinos.ruta) { InquilinosScreen(inquilinosVM,
                onCrear = { navController.navigate(Routes.CrearInquilino.ruta) },
                onEditar = { navController.navigate(Routes.ActualizarInquilino.crearRuta(it)) },
                onContratos = { navController.popBackStack() }) }
            composable(Routes.CrearInquilino.ruta) {
                InquilinoFormScreen(inquilinosVM) { navController.popBackStack() }
            }
            composable(Routes.ActualizarInquilino.ruta,
                arguments = listOf(navArgument("idInquilino") { type = NavType.IntType })) { item ->
                ActualizarInquilinoScreen(item.arguments?.getInt("idInquilino") ?: 0, inquilinosVM) {
                    navController.popBackStack()
                }
            }
            composable(Routes.CrearContrato.ruta,
                arguments = listOf(navArgument("idHabitacion") { type = NavType.IntType })) { item ->
                ContratoFormScreen(item.arguments?.getInt("idHabitacion") ?: 0,
                    contratosVM, habitacionesVM, inquilinosVM, propiedadesVM,
                    onVolver = { navController.popBackStack() },
                    onNuevoInquilino = { navController.navigate(Routes.CrearInquilino.ruta) })
            }
            composable(Routes.DetalleContrato.ruta,
                arguments = listOf(navArgument("idContrato") { type = NavType.IntType })) { item ->
                val id = item.arguments?.getInt("idContrato") ?: 0
                ContratoDetalleScreen(id, contratosVM, habitacionesVM, inquilinosVM, propiedadesVM,
                    onFinalizar = { navController.navigate(Routes.FinalizarContrato.crearRuta(id)) })
            }
            composable(Routes.FinalizarContrato.ruta,
                arguments = listOf(navArgument("idContrato") { type = NavType.IntType })) { item ->
                FinalizarContratoScreen(item.arguments?.getInt("idContrato") ?: 0, contratosVM) {
                    navController.popBackStack()
                }
            }
        }
    }
}
