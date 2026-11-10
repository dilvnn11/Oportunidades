package cl.duoc.oportunidades

import cl.duoc.oportunidades.dominio.Validadores
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadoresTest {
    @Test fun nombreVacio() = assertNotNull(Validadores.nombre(""))
    @Test fun nombreValido() = assertNull(Validadores.nombre("Feria Expo 2026"))
    @Test fun potencialFueraDeOpciones() = assertNotNull(Validadores.potencial("Gigante"))
    @Test fun potencialValido() = assertNull(Validadores.potencial("Alto"))
    @Test fun correoInvalido() = assertNotNull(Validadores.correo("ana@"))
    @Test fun claveFuerte() = assertNull(Validadores.clave("Duoc2025x"))
}
