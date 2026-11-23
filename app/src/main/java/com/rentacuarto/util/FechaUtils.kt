package com.rentacuarto.util

import java.time.LocalDate
import java.time.format.DateTimeParseException

/** Fechas ISO AAAA-MM-DD, como indica el modelo del proyecto. */
object FechaUtils {
    fun hoy(): String = LocalDate.now().toString()

    fun leer(fecha: String, nombreCampo: String): LocalDate {
        return try {
            val fechaLimpia = fecha.trim()
            require(Regex("""\d{4}-\d{2}-\d{2}""").matches(fechaLimpia)) {
                "Formato inválido"
            }
            LocalDate.parse(fechaLimpia)
        } catch (error: IllegalArgumentException) {
            throw IllegalArgumentException("$nombreCampo debe tener formato AAAA-MM-DD.")
        } catch (error: DateTimeParseException) {
            throw IllegalArgumentException("$nombreCampo no es una fecha válida.")
        }
    }
}
