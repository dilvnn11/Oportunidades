package cl.duoc.oportunidades

import cl.duoc.oportunidades.dominio.Opciones
import cl.duoc.oportunidades.dominio.Validadores
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/** Pruebas unitarias de las reglas de validación del formulario de oportunidad (sin Android). */
class ValidadoresOportunidadTest {
    // Nombre: obligatorio y mínimo 3 caracteres (se ignoran espacios de los extremos)
    @Test fun nombreVacioEsObligatorio() = assertEquals("El nombre de la oportunidad es obligatorio", Validadores.nombre(""))
    @Test fun nombreSoloEspaciosEsObligatorio() = assertNotNull(Validadores.nombre("   "))
    @Test fun nombreMuyCortoSeRechaza() = assertEquals("Mínimo 3 caracteres", Validadores.nombre("ab"))
    @Test fun nombreConEspaciosExtremosSeRecorta() = assertNotNull(Validadores.nombre("  ab "))
    @Test fun nombreDeTresCaracteresEsValido() = assertNull(Validadores.nombre("abc"))

    // Tipo: obligatorio y dentro de las opciones definidas por el equipo
    @Test fun tipoVacioEsObligatorio() = assertEquals("El tipo de oportunidad es obligatorio", Validadores.tipo(""))
    @Test fun tipoFueraDeOpcionesSeRechaza() = assertEquals("Selecciona un tipo válido", Validadores.tipo("Otro"))
    @Test fun todosLosTiposDefinidosSonValidos() = Opciones.tipos.forEach { assertNull(Validadores.tipo(it)) }

    // Descripción: obligatoria y mínimo 10 caracteres
    @Test fun descripcionVaciaEsObligatoria() = assertEquals("La descripción es obligatoria", Validadores.descripcion(""))
    @Test fun descripcionDeNueveCaracteresSeRechaza() = assertNotNull(Validadores.descripcion("123456789"))
    @Test fun descripcionDeDiezCaracteresEsValida() = assertNull(Validadores.descripcion("1234567890"))

    // Fuente de información: obligatoria
    @Test fun fuenteVaciaEsObligatoria() = assertNotNull(Validadores.fuente(" "))
    @Test fun fuenteConTextoEsValida() = assertNull(Validadores.fuente("Sitio web municipal"))

    // Prioridad, potencial y estado: obligatorios y dentro de las opciones
    @Test fun prioridadVaciaEsObligatoria() = assertEquals("La prioridad es obligatoria", Validadores.prioridad(""))
    @Test fun prioridadFueraDeOpcionesSeRechaza() = assertNotNull(Validadores.prioridad("Urgente"))
    @Test fun todasLasPrioridadesDefinidasSonValidas() = Opciones.prioridades.forEach { assertNull(Validadores.prioridad(it)) }
    @Test fun todosLosPotencialesDefinidosSonValidos() = Opciones.potenciales.forEach { assertNull(Validadores.potencial(it)) }
    @Test fun estadoVacioEsObligatorio() = assertEquals("El estado de gestión es obligatorio", Validadores.estado(""))
    @Test fun estadoFueraDeOpcionesSeRechaza() = assertNotNull(Validadores.estado("Cualquiera"))
    @Test fun estadoInicialDeUnaOportunidadNuevaEsValido() = assertNull(Validadores.estado("Evento identificado"))
    @Test fun todosLosEstadosDefinidosSonValidos() = Opciones.estados.forEach { assertNull(Validadores.estado(it)) }
}
