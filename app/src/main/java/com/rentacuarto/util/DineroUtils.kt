package com.rentacuarto.util

import java.math.BigDecimal
import java.math.RoundingMode

/** Los montos se guardan como Long en céntimos, no como Float o Double. */
object DineroUtils {
    fun convertirACentimos(texto: String): Long {
        val valor = texto.trim().replace(',', '.')
        require(valor.isNotBlank()) { "Ingresa la renta mensual." }
        val decimal = try {
            BigDecimal(valor)
        } catch (error: NumberFormatException) {
            throw IllegalArgumentException("La renta debe ser un número válido.")
        }
        require(decimal > BigDecimal.ZERO) { "La renta debe ser mayor que cero." }
        require(decimal.scale() <= 2) { "Utiliza como máximo dos decimales." }
        return try {
            decimal.movePointRight(2).longValueExact()
        } catch (error: ArithmeticException) {
            throw IllegalArgumentException("El monto es demasiado grande.")
        }
    }

    fun mostrar(centimos: Long): String {
        return "S/ " + BigDecimal.valueOf(centimos, 2)
            .setScale(2, RoundingMode.UNNECESSARY).toPlainString()
    }
}
