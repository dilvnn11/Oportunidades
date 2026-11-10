package cl.duoc.oportunidades.ui.componentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/** Solo presenta: el error llega ya calculado desde el ViewModel. */
@Composable
fun CampoValidado(
    valor: String, alCambiar: (String) -> Unit, etiqueta: String, icono: ImageVector, error: String?,
    modifier: Modifier = Modifier, esClave: Boolean = false, teclado: KeyboardType = KeyboardType.Text,
    unaLinea: Boolean = true, lineasMin: Int = 1
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = valor, onValueChange = alCambiar, modifier = modifier.fillMaxWidth(),
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, null) },
        trailingIcon = {
            when {
                error != null -> Icon(Icons.Filled.Error, "Error", tint = MaterialTheme.colorScheme.error)
                esClave -> IconButton(onClick = { visible = !visible }) {
                    Icon(if (visible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, "Mostrar u ocultar contraseña")
                }
                valor.isNotEmpty() -> Icon(Icons.Filled.CheckCircle, "Válido", tint = Color(0xFF2E7D32))
                else -> {}
            }
        },
        isError = error != null,
        supportingText = { AnimatedVisibility(error != null) { Text(error ?: "", color = MaterialTheme.colorScheme.error) } },
        visualTransformation = if (esClave && !visible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(keyboardType = if (esClave) KeyboardType.Password else teclado),
        singleLine = unaLinea, minLines = lineasMin
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoSelector(
    valor: String, opciones: List<String>, alSeleccionar: (String) -> Unit,
    etiqueta: String, icono: ImageVector, error: String?, modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = it }, modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor, onValueChange = {}, readOnly = true,
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            label = { Text(etiqueta) },
            leadingIcon = { Icon(icono, null) },
            trailingIcon = {
                if (error != null) Icon(Icons.Filled.Error, "Error", tint = MaterialTheme.colorScheme.error)
                else ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
            },
            isError = error != null,
            supportingText = { AnimatedVisibility(error != null) { Text(error ?: "", color = MaterialTheme.colorScheme.error) } }
        )
        ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            opciones.forEach { op ->
                DropdownMenuItem(text = { Text(op) }, onClick = { alSeleccionar(op); expandido = false })
            }
        }
    }
}
