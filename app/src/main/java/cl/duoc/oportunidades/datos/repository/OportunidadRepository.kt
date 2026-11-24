package cl.duoc.oportunidades.datos.repository

import cl.duoc.oportunidades.datos.local.OportunidadDao
import cl.duoc.oportunidades.datos.local.OportunidadEntity
import cl.duoc.oportunidades.datos.remote.RetrofitClient
import cl.duoc.oportunidades.datos.remote.api.ExternalApiService
import cl.duoc.oportunidades.datos.remote.api.OportunidadApiService
import cl.duoc.oportunidades.datos.remote.dto.MindicadorResponse
import cl.duoc.oportunidades.datos.remote.dto.OportunidadDto
import kotlinx.coroutines.flow.Flow

class OportunidadRepository(
    private val dao: OportunidadDao,
    private val apiService: OportunidadApiService = RetrofitClient.oportunidadApi,
    private val externalApiService: ExternalApiService = RetrofitClient.externalApi
) {
    fun observarTodas(): Flow<List<OportunidadEntity>> = dao.observarTodas()

    suspend fun obtener(id: Long): OportunidadEntity? = dao.obtener(id)

    suspend fun guardar(o: OportunidadEntity) {
        val entidadActualizada = o.copy(actualizadoEn = System.currentTimeMillis())
        dao.guardar(entidadActualizada)
        try {
            val dto = OportunidadDto.fromEntity(entidadActualizada)
            if (entidadActualizada.id == 0L) {
                val res = apiService.crear(dto)
                if (res.isSuccessful && res.body() != null) {
                    val guardadoRemoto = res.body()!!.toEntity()
                    dao.guardar(guardadoRemoto)
                }
            } else {
                apiService.actualizar(entidadActualizada.id, dto)
            }
        } catch (_: Exception) {
            // Manejo silencioso en caso de no disponibilidad de red/backend
        }
    }

    suspend fun sincronizarConServidor() {
        try {
            val response = apiService.listarTodas()
            if (response.isSuccessful && response.body() != null) {
                val remotos: List<OportunidadEntity> = response.body()!!.map { it.toEntity() }
                for (item: OportunidadEntity in remotos) {
                    dao.guardar(item)
                }
            }
        } catch (_: Exception) {
            // Si la llamada falla, se mantiene la base de datos local intacta
        }
    }

    suspend fun obtenerIndicadoresExternos(): MindicadorResponse? {
        return try {
            val response = externalApiService.obtenerIndicadores()
            if (response.isSuccessful) response.body() else null
        } catch (_: Exception) {
            null
        }
    }
}
