package com.rentacuarto.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Contrato entre una habitación y un inquilino.
 * Fechas ISO: AAAA-MM-DD. Renta monetaria en céntimos de sol.
 * Solo fechaFinReal finaliza un contrato; fechaFinPactada es informativa.
 */
@Entity(
    tableName = "contratos",
    foreignKeys = [
        ForeignKey(
            entity = HabitacionEntity::class,
            parentColumns = ["id"], childColumns = ["habitacion_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = InquilinoEntity::class,
            parentColumns = ["id"], childColumns = ["inquilino_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ],
    indices = [Index(value = ["habitacion_id"]), Index(value = ["inquilino_id"])]
)
data class ContratoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "habitacion_id") val habitacionId: Int,
    @ColumnInfo(name = "inquilino_id") val inquilinoId: Int,
    @ColumnInfo(name = "fecha_inicio") val fechaInicio: String,
    @ColumnInfo(name = "fin_pactada") val fechaFinPactada: String? = null,
    @ColumnInfo(name = "fin_real") val fechaFinReal: String? = null,
    @ColumnInfo(name = "primer_vencimiento") val primerVencimiento: String,
    @ColumnInfo(name = "renta_mensual_cent") val rentaMensualCent: Long,
    @ColumnInfo(name = "n_garantia") val numeroRentasGarantia: Int = 0,
    @ColumnInfo(name = "n_adelantadas") val numeroRentasAdelantadas: Int = 0
)
