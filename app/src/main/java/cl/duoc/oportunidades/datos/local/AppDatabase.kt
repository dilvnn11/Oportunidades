package cl.duoc.oportunidades.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OportunidadEntity::class, UsuarioEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun oportunidadDao(): OportunidadDao
    abstract fun usuarioDao(): UsuarioDao

    companion object {
        @Volatile private var instancia: AppDatabase? = null
        fun obtener(context: Context): AppDatabase = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "oportunidades.db"
            ).build().also { instancia = it }
        }
    }
}
