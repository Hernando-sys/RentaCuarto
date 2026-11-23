package com.rentacuarto.demo

import android.app.Application
import android.util.Log
import com.rentacuarto.data.local.RentaCuartoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Solo debug: carga datos falsos en la base aislada de com.rentacuarto.debug. */
class AplicacionDesarrollo: Application() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun onCreate() {
        super.onCreate()
        scope.launch {
            try {
                DatosDesarrollo.cargar(RentaCuartoDatabase.obtener(this@AplicacionDesarrollo))
            } catch (error: Exception) {
                Log.e("RENTACUARTO_DEBUG", "Error cargando muestra", error)
            }
        }
    }
}
