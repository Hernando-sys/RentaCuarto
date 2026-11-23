package com.rentacuarto.data.local.entity

import androidx.room.*

/** Gastos anulados no se eliminan físicamente del historial. */
@Entity(tableName="gastos",foreignKeys=[ForeignKey(entity=PropiedadEntity::class,
    parentColumns=["id"],childColumns=["propiedad_id"],onDelete=ForeignKey.RESTRICT)],
    indices=[Index(value=["propiedad_id"]),Index(value=["propiedad_id","fecha"])])
data class GastoEntity(
    @PrimaryKey(autoGenerate=true) val id: Int=0,
    @ColumnInfo(name="propiedad_id") val propiedadId: Int,
    val categoria: String,
    val fecha: String,
    val descripcion: String,
    @ColumnInfo(name="monto_cent") val montoCent: Long,
    val anulado: Boolean=false
)
