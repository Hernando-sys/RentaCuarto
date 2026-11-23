package com.rentacuarto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.rentacuarto.navigation.AppNavigation
import com.rentacuarto.ui.theme.RentaCuartoTheme

/** Punto de entrada: aquí solo iniciamos la interfaz de la aplicación. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentaCuartoTheme {
                AppNavigation()
            }
        }
    }
}
