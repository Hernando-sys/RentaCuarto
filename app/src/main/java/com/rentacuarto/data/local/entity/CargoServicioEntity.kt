package com.rentacuarto.data.local.entity

import androidx.room.*

/** Un cargo anulado se conserva para mantener la trazabilidad. */
@Entity(tableName="cargos_servicio", foreignKeys=[ForeignKey(entity=RentaEntity::class,
    parentColumns=["id"],childColumns=["renta_id"],onDelete=ForeignKey.RESTRICT)],
    indices=[Index(value=["renta_id"])])
data class CargoServicioEntity(
    @PrimaryKey(autoGenerate=true) val id: Int=0,
    @ColumnInfo(name="renta_id") val rentaId: Int,
    val tipo: String,
    val descripcion: String="",
    @ColumnInfo(name="monto_cent") val montoCent: Long,
    val anulado: Boolean=false
)
