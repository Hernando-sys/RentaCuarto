package com.rentacuarto.navigation

/** Navegación exclusiva de las pantallas P01 a P12 del prototipo. */
sealed class Routes(val ruta: String) {
    object Inicio : Routes("inicio")
    object Alertas : Routes("alertas")
    object Propiedades : Routes("propiedades")
    object CrearPropiedad : Routes("crearPropiedad")
    object DetallePropiedad : Routes("detallePropiedad/{idPropiedad}") {
        fun crearRuta(id: Int) = "detallePropiedad/$id"
    }
    object ActualizarPropiedad : Routes("actualizarPropiedad/{idPropiedad}") {
        fun crearRuta(id: Int) = "actualizarPropiedad/$id"
    }
    object CrearHabitacion : Routes("crearHabitacion/{idPropiedad}") {
        fun crearRuta(id: Int) = "crearHabitacion/$id"
    }
    object ActualizarHabitacion : Routes("actualizarHabitacion/{idHabitacion}") {
        fun crearRuta(id: Int) = "actualizarHabitacion/$id"
    }
    object Inquilinos : Routes("inquilinos")
    object CrearInquilino : Routes("crearInquilino")
    object ActualizarInquilino : Routes("actualizarInquilino/{idInquilino}") {
        fun crearRuta(id: Int) = "actualizarInquilino/$id"
    }
    object Contratos : Routes("contratos")
    object CrearContrato : Routes("crearContrato/{idHabitacion}") {
        fun crearRuta(id: Int = 0) = "crearContrato/$id"
    }
    object DetalleContrato : Routes("detalleContrato/{idContrato}") {
        fun crearRuta(id: Int) = "detalleContrato/$id"
    }
    object FinalizarContrato : Routes("finalizarContrato/{idContrato}") {
        fun crearRuta(id: Int) = "finalizarContrato/$id"
    }
}
