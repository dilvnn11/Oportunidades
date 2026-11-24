package cl.duoc.oportunidades

import cl.duoc.oportunidades.datos.local.OportunidadDao
import cl.duoc.oportunidades.datos.local.OportunidadEntity
import cl.duoc.oportunidades.datos.remote.api.ExternalApiService
import cl.duoc.oportunidades.datos.remote.api.OportunidadApiService
import cl.duoc.oportunidades.datos.remote.dto.IndicadorDetalle
import cl.duoc.oportunidades.datos.remote.dto.MindicadorResponse
import cl.duoc.oportunidades.datos.remote.dto.OportunidadDto
import cl.duoc.oportunidades.datos.repository.OportunidadRepository
import io.mockk.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class OportunidadRepositoryTest {

    private val dao = mockk<OportunidadDao>(relaxed = true)
    private val apiService = mockk<OportunidadApiService>(relaxed = true)
    private val externalApiService = mockk<ExternalApiService>(relaxed = true)

    private lateinit var repository: OportunidadRepository

    @Before
    fun setUp() {
        clearAllMocks()
        repository = OportunidadRepository(dao, apiService, externalApiService)
    }

    @Test
    fun observarTodas_retornaFlowDesdeDao() {
        runBlocking {
            val listaMock = listOf(
                OportunidadEntity(id = 1L, nombre = "Venta Equipos", tipo = "Tecnología", descripcion = "Venta corporativa", fuente = "Licitación", prioridad = "Alta", potencial = "Medio", estado = "En negociación", observaciones = "")
            )
            every { dao.observarTodas() } returns flowOf(listaMock)

            val resultado = repository.observarTodas()
            resultado.collect { lista ->
                assertEquals(1, lista.size)
                assertEquals("Venta Equipos", lista[0].nombre)
            }
        }
    }

    @Test
    fun guardar_insertaEnDao_yLlamaApiService() {
        runBlocking {
            val entidad = OportunidadEntity(id = 0L, nombre = "Licitación Redes", tipo = "Servicios", descripcion = "Mantención de servidores", fuente = "Portal", prioridad = "Alta", potencial = "Alto", estado = "Propuesta enviada", observaciones = "")
            coEvery { apiService.crear(any()) } returns Response.success(OportunidadDto.fromEntity(entidad.copy(id = 10L)))

            repository.guardar(entidad)

            coVerify { dao.guardar(any()) }
            coVerify { apiService.crear(any()) }
        }
    }

    @Test
    fun obtenerIndicadoresExternos_retornaMindicadorResponse() {
        runBlocking {
            val mockResponse = MindicadorResponse(
                version = "1.0",
                uf = IndicadorDetalle("uf", "Unidad de Fomento", "Pesos", "2026-03-30", 38000.0)
            )
            coEvery { externalApiService.obtenerIndicadores() } returns Response.success(mockResponse)

            val resultado = repository.obtenerIndicadoresExternos()
            assertNotNull(resultado)
            assertEquals(38000.0, resultado?.uf?.valor ?: 0.0, 0.1)
        }
    }
}
