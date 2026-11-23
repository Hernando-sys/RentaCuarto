package com.rentacuarto.data.local.entity

import androidx.room.*

/** Origen EFECTIVO / GARANTIA se distingue del medio YAPE / EFECTIVO / TRANSFERENCIA. */
@Entity(tableName="abonos", foreignKeys=[
    ForeignKey(entity=ContratoEntity::class,parentColumns=["id"],childColumns=["contrato_id"],onDelete=ForeignKey.RESTRICT),
    ForeignKey(entity=GarantiaEntity::class,parentColumns=["id"],childColumns=["garantia_id"],onDelete=ForeignKey.RESTRICT)],
    indices=[Index(value=["contrato_id"]),Index(value=["garantia_id"])])
data class AbonoEntity(
    @PrimaryKey(autoGenerate=true) val id: Int=0,
    @ColumnInfo(name="contrato_id") val contratoId: Int,
    @ColumnInfo(name="garantia_id") val garantiaId: Int?=null,
    val fecha: String,
    @ColumnInfo(name="monto_cent") val montoCent: Long,
    val medio: String,
    val origen: String="EFECTIVO",
    val pagador: String?=null,
    val anulado: Boolean=false
)
