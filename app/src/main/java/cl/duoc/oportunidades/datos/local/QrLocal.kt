package cl.duoc.oportunidades.datos.local

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Tabla del historial de códigos QR leídos (persistencia con Room). */
@Entity(tableName = "qr_escaneos")
data class QrEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val contenido: String,
    val escaneadoEn: Long = System.currentTimeMillis()
)

@Dao
interface QrDao {
    @Insert
    suspend fun insertar(qr: QrEntity)

    @Query("SELECT * FROM qr_escaneos ORDER BY escaneadoEn DESC")
    fun observarTodos(): Flow<List<QrEntity>>

    @Query("DELETE FROM qr_escaneos")
    suspend fun borrarTodos()
}
