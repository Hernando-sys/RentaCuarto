package com.rentacuarto.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Una propiedad es una casa o edificio que contiene habitaciones. */
@Entity(tableName = "propiedades")
data class PropiedadEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val direccion: String,
    val tipo: String, // CASA o EDIFICIO
    val notas: String = ""
)
