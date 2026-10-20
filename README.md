# Gestión móvil de oportunidades y activaciones corporativas

**Caso:** DSY1105_007D  
**Evaluación:** Parcial 2 – Módulo Oportunidades  
**Integrantes:** Ariel Catalan (Integrante A en Trello) · Deland Cabello (Integrante B en Trello)  
**Equipo N°:** 3  
**Trello:** https://trello.com/b/6ad6455041149ef82eb3bd48

## Funcionalidades implementadas
- Registro e inicio de sesión (usuarios guardados en Room, contraseña con hash SHA-256).
- Menú principal (Oportunidades activo; Eventos, Organizaciones y Contactos quedan para el examen transversal).
- **ListaOportunidadesScreen:** lista reactiva (nombre, tipo, prioridad, estado), botón de agregar y selección para editar.
- **OportunidadFormScreen:** crear y modificar con nombre, tipo, descripción, fuente, prioridad, potencial comercial, estado y observaciones.
- Validaciones por campo con ícono y mensaje de error, controladas desde el ViewModel.
- Recursos nativos: **cámara** (foto de evidencia) y **ubicación** (referencia geográfica, con permiso en tiempo de ejecución).
- Animaciones: transiciones de navegación, errores animados, cambio de color de prioridad, reordenamiento de la lista.

## Arquitectura (MVVM real)
```
datos/local        OportunidadEntity, UsuarioEntity, OportunidadDao, UsuarioDao, AppDatabase (Room)
datos/repository   OportunidadRepository, UsuarioRepository
dominio            Validadores, Opciones (reglas puras, sin Android)
ui/oportunidad     ListaOportunidadesScreen, OportunidadFormScreen, OportunidadViewModel, OportunidadUiState
ui/auth, ui/menu   Login, Registro, Menú, AuthViewModel, AuthUiState
ui/componentes     Campos de formulario reutilizables (solo presentan)
ui/navegacion      AppNavigation
MainActivity.kt
```
Flujo de datos: Screen → ViewModel → Repository → DAO → Room, y de vuelta con `Flow` → `StateFlow` → UI.

## Recursos nativos
- **Cámara:** abre la app de cámara del sistema (`TakePicturePreview`); no requiere permiso de cámara. La imagen obtenida es una miniatura que se guarda en el almacenamiento interno de la app y su ruta queda en Room.
- **Ubicación:** pide el permiso `ACCESS_FINE_LOCATION` en tiempo de ejecución y guarda latitud y longitud de la oportunidad. Usa la última ubicación conocida del dispositivo; si no hay ninguna, muestra un mensaje.

## Ejecutar
1. Android Studio Koala o superior, JDK 17. `File > Open` sobre la carpeta del proyecto y esperar el Gradle Sync.
2. Emulador API 26+ (en *Extended controls > Location* fija una ubicación antes de probar la referencia geográfica).
3. Tests unitarios: `./gradlew test`, o en Android Studio clic derecho sobre `app/src/test` → *Run Tests*.

## Pruebas unitarias
`ValidadoresTest`, `ValidadoresOportunidadTest` y `ValidadoresAuthTest` verifican las reglas de `dominio/Validadores` (campos obligatorios, largos mínimos, opciones válidas, correo, contraseña y confirmación).
