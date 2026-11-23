package com.rentacuarto.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** No guardamos 'ocupada': se calcula consultando los contratos. */
@Entity(
    tableName = "habitaciones",
    foreignKeys = [ForeignKey(
        entity = PropiedadEntity::class,
        parentColumns = ["id"],
        childColumns = ["propiedad_id"],
        onDelete = ForeignKey.RESTRICT
    )],
    indices = [
        Index(value = ["propiedad_id"]),
        Index(value = ["propiedad_id", "codigo"], unique = true)
    ]
)
data class HabitacionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "propiedad_id") val propiedadId: Int,
    val codigo: String,
    val descripcion: String = ""
)
