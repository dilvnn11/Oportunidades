package cl.duoc.oportunidades.datos.repository

import cl.duoc.oportunidades.datos.local.OportunidadDao
import cl.duoc.oportunidades.datos.local.OportunidadEntity
import kotlinx.coroutines.flow.Flow

class OportunidadRepository(private val dao: OportunidadDao) {
    fun observarTodas(): Flow<List<OportunidadEntity>> = dao.observarTodas()
    suspend fun obtener(id: Long): OportunidadEntity? = dao.obtener(id)
    suspend fun guardar(o: OportunidadEntity) = dao.guardar(o.copy(actualizadoEn = System.currentTimeMillis()))
}
