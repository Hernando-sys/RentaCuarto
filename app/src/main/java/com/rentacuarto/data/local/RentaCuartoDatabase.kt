package com.rentacuarto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rentacuarto.data.local.dao.*
import com.rentacuarto.data.local.entity.*

/** Base local reducida del primer avance: cuatro tablas y ninguna tabla económica. */
@Database(
    entities = [PropiedadEntity::class, HabitacionEntity::class,
        InquilinoEntity::class, ContratoEntity::class],
    version = 1, exportSchema = false
)
abstract class RentaCuartoDatabase : RoomDatabase() {
    abstract fun propiedadDao(): PropiedadDao
    abstract fun habitacionDao(): HabitacionDao
    abstract fun inquilinoDao(): InquilinoDao
    abstract fun contratoDao(): ContratoDao

    companion object {
        @Volatile private var instancia: RentaCuartoDatabase? = null

        fun obtener(contexto: Context): RentaCuartoDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext, RentaCuartoDatabase::class.java,
                    "rentacuarto_expo.db"
                ).build().also { instancia = it }
            }
    }
}
