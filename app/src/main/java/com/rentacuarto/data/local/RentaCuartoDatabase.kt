package com.rentacuarto.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rentacuarto.data.local.dao.*
import com.rentacuarto.data.local.entity.*

/**
 * Base local oficial: diez entidades persistentes del documento UML.
 * v1: propiedades, habitaciones, inquilinos y contratos.
 * v2: agrega seis tablas económicas SIN modificar las tablas v1.
 * La actualización es incremental: no se permite fallback destructivo.
 */
@Database(
    entities = [PropiedadEntity::class, HabitacionEntity::class,
        InquilinoEntity::class, ContratoEntity::class, RentaEntity::class,
        CargoServicioEntity::class, AbonoEntity::class, AplicacionAbonoEntity::class,
        GarantiaEntity::class, GastoEntity::class],
    version = 2, exportSchema = false
)
abstract class RentaCuartoDatabase : RoomDatabase() {
    abstract fun propiedadDao(): PropiedadDao
    abstract fun habitacionDao(): HabitacionDao
    abstract fun inquilinoDao(): InquilinoDao
    abstract fun contratoDao(): ContratoDao
    abstract fun rentaDao(): RentaDao
    abstract fun cargoServicioDao(): CargoServicioDao
    abstract fun abonoDao(): AbonoDao
    abstract fun aplicacionAbonoDao(): AplicacionAbonoDao
    abstract fun garantiaDao(): GarantiaDao
    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile private var instancia: RentaCuartoDatabase? = null

        val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""CREATE TABLE IF NOT EXISTS `rentas` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `contrato_id` INTEGER NOT NULL, `periodo` TEXT NOT NULL, `fecha_vencimiento` TEXT NOT NULL, `renta_base_cent` INTEGER NOT NULL, FOREIGN KEY(`contrato_id`) REFERENCES `contratos`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_rentas_contrato_id` ON `rentas` (`contrato_id`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_rentas_contrato_id_periodo` ON `rentas` (`contrato_id`, `periodo`)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS `garantias` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `contrato_id` INTEGER NOT NULL, `pactado_cent` INTEGER NOT NULL, `recibido_cent` INTEGER NOT NULL, `devuelto_cent` INTEGER NOT NULL, `aplicado_cent` INTEGER NOT NULL, `cerrado` INTEGER NOT NULL, FOREIGN KEY(`contrato_id`) REFERENCES `contratos`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT)""")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_garantias_contrato_id` ON `garantias` (`contrato_id`)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS `cargos_servicio` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `renta_id` INTEGER NOT NULL, `tipo` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `monto_cent` INTEGER NOT NULL, `anulado` INTEGER NOT NULL, FOREIGN KEY(`renta_id`) REFERENCES `rentas`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_cargos_servicio_renta_id` ON `cargos_servicio` (`renta_id`)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS `abonos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `contrato_id` INTEGER NOT NULL, `garantia_id` INTEGER, `fecha` TEXT NOT NULL, `monto_cent` INTEGER NOT NULL, `medio` TEXT NOT NULL, `origen` TEXT NOT NULL, `pagador` TEXT, `anulado` INTEGER NOT NULL, FOREIGN KEY(`contrato_id`) REFERENCES `contratos`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT, FOREIGN KEY(`garantia_id`) REFERENCES `garantias`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_abonos_contrato_id` ON `abonos` (`contrato_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_abonos_garantia_id` ON `abonos` (`garantia_id`)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS `aplicaciones_abono` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `abono_id` INTEGER NOT NULL, `renta_id` INTEGER NOT NULL, `monto_cent` INTEGER NOT NULL, FOREIGN KEY(`abono_id`) REFERENCES `abonos`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT, FOREIGN KEY(`renta_id`) REFERENCES `rentas`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_aplicaciones_abono_abono_id` ON `aplicaciones_abono` (`abono_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_aplicaciones_abono_renta_id` ON `aplicaciones_abono` (`renta_id`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_aplicaciones_abono_abono_id_renta_id` ON `aplicaciones_abono` (`abono_id`, `renta_id`)")
                db.execSQL("""CREATE TABLE IF NOT EXISTS `gastos` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `propiedad_id` INTEGER NOT NULL, `categoria` TEXT NOT NULL, `fecha` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `monto_cent` INTEGER NOT NULL, `anulado` INTEGER NOT NULL, FOREIGN KEY(`propiedad_id`) REFERENCES `propiedades`(`id`) ON UPDATE NO ACTION ON DELETE RESTRICT)""")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_gastos_propiedad_id` ON `gastos` (`propiedad_id`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_gastos_propiedad_id_fecha` ON `gastos` (`propiedad_id`, `fecha`)")
                // Backfill: los contratos existentes solo registran garantía PACTADA.
                db.execSQL("""INSERT INTO garantias (contrato_id,pactado_cent,recibido_cent,devuelto_cent,aplicado_cent,cerrado)
                    SELECT id, renta_mensual_cent*n_garantia, 0, 0, 0, 0
                    FROM contratos WHERE n_garantia > 0""")
            }
        }

        fun obtener(contexto: Context): RentaCuartoDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(contexto.applicationContext,
                    RentaCuartoDatabase::class.java, "rentacuarto.db")
                    .addMigrations(MIGRACION_1_2)
                    .build().also { instancia = it }
            }
    }
}
