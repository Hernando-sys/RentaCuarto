package com.rentacuarto.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// Los wireframes son claros; usamos una paleta fija, también en modo oscuro.
private val ColorSuperficieSuave = androidx.compose.ui.graphics.Color(0xFFF7F9FA)

private val ColoresRenta = lightColorScheme(
    primary = VerdeRenta,
    onPrimary = BlancoRenta,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeOscuro,
    secondary = VerdeOscuro,
    onSecondary = BlancoRenta,
    secondaryContainer = AzulClaro,
    onSecondaryContainer = AzulTexto,
    background = FondoRenta,
    onBackground = TextoRenta,
    surface = BlancoRenta,
    onSurface = TextoRenta,
    surfaceVariant = ColorSuperficieSuave,
    onSurfaceVariant = GrisTexto,
    outline = BordeRenta,
    outlineVariant = BordeRenta,
    error = RojoRenta,
    onError = BlancoRenta,
    errorContainer = RojoClaro,
    onErrorContainer = RojoRenta
)

private val FormasRenta = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(11.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun RentaCuartoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColoresRenta, typography = Typography,
        shapes = FormasRenta, content = content)
}
