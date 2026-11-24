package cl.duoc.oportunidades

import android.app.Application
import cl.duoc.oportunidades.datos.remote.dto.IndicadorDetalle
import cl.duoc.oportunidades.datos.remote.dto.MindicadorResponse
import cl.duoc.oportunidades.datos.repository.OportunidadRepository
import cl.duoc.oportunidades.ui.oportunidad.OportunidadViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OportunidadViewModelTest {

    private val application = mockk<Application>(relaxed = true)
    private val repository = mockk<OportunidadRepository>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: OportunidadViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()

        every { repository.observarTodas() } returns flowOf(emptyList())
        coEvery { repository.obtenerIndicadoresExternos() } returns MindicadorResponse(
            uf = IndicadorDetalle("uf", "UF", "Pesos", "2026-03-30", 38000.0)
        )

        viewModel = OportunidadViewModel(application, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onNombre_actualizaUiState_yValida() {
        viewModel.onNombre("Proyecto Alfa")
        assertEquals("Proyecto Alfa", viewModel.uiState.value.nombre)
        assertNull(viewModel.uiState.value.nombreError)
    }

    @Test
    fun onNombre_demasiadoCorto_retornaError() {
        viewModel.onNombre("Ab")
        assertEquals("Ab", viewModel.uiState.value.nombre)
        assertNotNull(viewModel.uiState.value.nombreError)
    }

    @Test
    fun cargarIndicadoresExternos_actualizaStateConRespuesta() {
        runTest {
            viewModel.cargarIndicadoresExternos()
            advanceUntilIdle()

            val state = viewModel.uiState.value
            assertNotNull(state.indicadores)
            assertEquals(38000.0, state.indicadores?.uf?.valor ?: 0.0, 0.1)
            assertFalse(state.cargandoIndicadores)
        }
    }

    @Test
    fun guardar_conCamposValidos_ejecutaGuardadoEnRepo() {
        runTest {
            viewModel.onNombre("Licitación Software")
            viewModel.onTipo("Feria comercial")
            viewModel.onDescripcion("Desarrollo de módulo corporativo")
            viewModel.onFuente("Cliente habitual")
            viewModel.onPrioridad("Alta")
            viewModel.onPotencial("Alto")
            viewModel.onEstado("Propuesta enviada")

            viewModel.guardar()
            advanceUntilIdle()

            coVerify { repository.guardar(any()) }
            assertTrue(viewModel.uiState.value.guardadoExitosamente)
        }
    }
}
