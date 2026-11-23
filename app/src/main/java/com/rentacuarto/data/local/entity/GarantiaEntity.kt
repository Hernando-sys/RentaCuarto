package com.rentacuarto.data.local.entity

import androidx.room.*

/** El pacto de garantía NO implica recepción efectiva de dinero (RN14). */
@Entity(tableName="garantias", foreignKeys=[ForeignKey(entity=ContratoEntity::class,
    parentColumns=["id"],childColumns=["contrato_id"],onDelete=ForeignKey.RESTRICT)],
    indices=[Index(value=["contrato_id"],unique=true)])
data class GarantiaEntity(
    @PrimaryKey(autoGenerate=true) val id: Int=0,
    @ColumnInfo(name="contrato_id") val contratoId: Int,
    @ColumnInfo(name="pactado_cent") val pactadoCent: Long,
    @ColumnInfo(name="recibido_cent") val recibidoCent: Long=0,
    @ColumnInfo(name="devuelto_cent") val devueltoCent: Long=0,
    @ColumnInfo(name="aplicado_cent") val aplicadoCent: Long=0,
    val cerrado: Boolean=false
)
