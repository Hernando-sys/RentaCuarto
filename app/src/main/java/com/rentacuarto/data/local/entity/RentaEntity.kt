package com.rentacuarto.data.local.entity

import androidx.room.*

/** Una renta por contrato y periodo AAAA-MM; base histórica inmutable. */
@Entity(tableName = "rentas", foreignKeys = [ForeignKey(entity = ContratoEntity::class,
    parentColumns = ["id"], childColumns = ["contrato_id"], onDelete = ForeignKey.RESTRICT)],
    indices = [Index(value=["contrato_id"]), Index(value=["contrato_id","periodo"], unique=true)])
data class RentaEntity(
    @PrimaryKey(autoGenerate=true) val id: Int = 0,
    @ColumnInfo(name="contrato_id") val contratoId: Int,
    val periodo: String,
    @ColumnInfo(name="fecha_vencimiento") val fechaVencimiento: String,
    @ColumnInfo(name="renta_base_cent") val rentaBaseCent: Long
)
