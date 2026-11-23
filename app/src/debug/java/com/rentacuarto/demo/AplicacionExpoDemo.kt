package com.rentacuarto.demo

import android.app.Application
import android.util.Log
import com.rentacuarto.data.local.RentaCuartoDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Solo debug: carga datos falsos en la base aislada de com.rentacuarto.expo.demo. */
class AplicacionExpoDemo: Application() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    override fun onCreate() {
        super.onCreate()
        scope.launch {
            try {
                DatosExpo.cargar(RentaCuartoDatabase.obtener(this@AplicacionExpoDemo))
            } catch (error: Exception) {
                Log.e("RENTACUARTO_EXPO", "Error cargando muestra", error)
            }
        }
    }
}
