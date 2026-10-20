package cl.duoc.oportunidades.datos.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface OportunidadDao {
    /** Inserta si id = 0, actualiza si ya existe. */
    @Upsert
    suspend fun guardar(oportunidad: OportunidadEntity)

    @Query("SELECT * FROM oportunidades ORDER BY actualizadoEn DESC")
    fun observarTodas(): Flow<List<OportunidadEntity>>

    @Query("SELECT * FROM oportunidades WHERE id = :id LIMIT 1")
    suspend fun obtener(id: Long): OportunidadEntity?
}
