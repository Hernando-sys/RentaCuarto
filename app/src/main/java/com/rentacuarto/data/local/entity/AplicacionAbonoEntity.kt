package com.rentacuarto.data.local.entity

import androidx.room.*

/** Registra qué parte de un abono cubre cada renta; sin inventar saldo ficticio. */
@Entity(tableName="aplicaciones_abono", foreignKeys=[
    ForeignKey(entity=AbonoEntity::class,parentColumns=["id"],childColumns=["abono_id"],onDelete=ForeignKey.RESTRICT),
    ForeignKey(entity=RentaEntity::class,parentColumns=["id"],childColumns=["renta_id"],onDelete=ForeignKey.RESTRICT)],
    indices=[Index(value=["abono_id"]),Index(value=["renta_id"]),Index(value=["abono_id","renta_id"],unique=true)])
data class AplicacionAbonoEntity(
    @PrimaryKey(autoGenerate=true) val id: Int=0,
    @ColumnInfo(name="abono_id") val abonoId: Int,
    @ColumnInfo(name="renta_id") val rentaId: Int,
    @ColumnInfo(name="monto_cent") val montoCent: Long
)
