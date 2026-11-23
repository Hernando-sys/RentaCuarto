package com.rentacuarto.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/** DNI es String para mantener sus ocho dígitos. */
@Entity(tableName = "inquilinos", indices = [Index(value = ["dni"], unique = true)])
data class InquilinoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombres: String,
    val dni: String,
    val telefono: String
)
