package cl.duoc.oportunidades

import cl.duoc.oportunidades.dominio.Validadores
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/** Pruebas unitarias de las reglas de validación de registro e inicio de sesión (sin Android). */
class ValidadoresAuthTest {
    // Nombre de la persona: obligatorio, mínimo 3 caracteres, solo letras y espacios
    @Test fun nombrePersonaVacioEsObligatorio() = assertEquals("El nombre es obligatorio", Validadores.nombrePersona(""))
    @Test fun nombrePersonaMuyCortoSeRechaza() = assertEquals("Mínimo 3 caracteres", Validadores.nombrePersona("Al"))
    @Test fun nombrePersonaConNumerosSeRechaza() = assertEquals("Solo letras y espacios", Validadores.nombrePersona("Ana123"))
    @Test fun nombrePersonaConTildeEsValido() = assertNull(Validadores.nombrePersona("Ana Pérez"))

    // Correo: obligatorio y con formato válido
    @Test fun correoVacioEsObligatorio() = assertEquals("El correo es obligatorio", Validadores.correo(""))
    @Test fun correoSinDominioSeRechaza() = assertNotNull(Validadores.correo("ana@"))
    @Test fun correoSinExtensionSeRechaza() = assertEquals("Formato de correo inválido", Validadores.correo("ana@duoc"))
    @Test fun correoValidoSeAcepta() = assertNull(Validadores.correo("ana@duoc.cl"))
    @Test fun correoConEspaciosExtremosSeRecorta() = assertNull(Validadores.correo(" ana@duoc.cl "))

    // Contraseña: mínimo 8 caracteres, al menos un número y una mayúscula
    @Test fun claveVaciaEsObligatoria() = assertEquals("La contraseña es obligatoria", Validadores.clave(""))
    @Test fun claveCortaSeRechaza() = assertEquals("Mínimo 8 caracteres", Validadores.clave("Abc12"))
    @Test fun claveSinNumeroSeRechaza() = assertEquals("Debe incluir al menos un número", Validadores.clave("Abcdefgh"))
    @Test fun claveSinMayusculaSeRechaza() = assertEquals("Debe incluir una mayúscula", Validadores.clave("abcdefg1"))
    @Test fun claveFuerteSeAcepta() = assertNull(Validadores.clave("Duoc2025x"))

    // Confirmación de contraseña
    @Test fun confirmacionVaciaSeRechaza() = assertEquals("Confirma tu contraseña", Validadores.confirmacion("Duoc2025x", ""))
    @Test fun confirmacionDistintaSeRechaza() = assertEquals("Las contraseñas no coinciden", Validadores.confirmacion("Duoc2025x", "Duoc2025y"))
    @Test fun confirmacionIgualSeAcepta() = assertNull(Validadores.confirmacion("Duoc2025x", "Duoc2025x"))
}
