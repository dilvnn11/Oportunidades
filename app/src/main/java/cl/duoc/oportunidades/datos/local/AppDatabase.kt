package cl.duoc.oportunidades.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [OportunidadEntity::class, UsuarioEntity::class, QrEntity::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun oportunidadDao(): OportunidadDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun qrDao(): QrDao

    companion object {
        /** v1 -> v2: agrega la tabla del historial de QR sin perder oportunidades ni usuarios. */
        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `qr_escaneos` " +
                        "(`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`contenido` TEXT NOT NULL, `escaneadoEn` INTEGER NOT NULL)"
                )
            }
        }

        @Volatile private var instancia: AppDatabase? = null
        fun obtener(context: Context): AppDatabase = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "oportunidades.db"
            ).addMigrations(MIGRACION_1_2).build().also { instancia = it }
        }
    }
}
