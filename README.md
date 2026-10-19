# Gestión móvil de oportunidades y activaciones corporativas

**Caso:** DSY1105_007D · Primera entrega (Evaluación Parcial 2) · Módulo **Oportunidades**
**Integrantes:** _Nombre Apellido_ · _Nombre Apellido_  **Equipo N°:** _X_  **Trello:** _enlace_

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
datos/local        OportunidadEntity, OportunidadDao, AppDatabase (Room)
datos/repository   OportunidadRepository, UsuarioRepository
dominio            Validadores, Opciones (reglas puras, sin Android)
ui/oportunidad     ListaOportunidadesScreen, OportunidadFormScreen, OportunidadViewModel (UiState + StateFlow)
ui/auth, ui/menu   Login, Registro, Menú
ui/navegacion      AppNavigation
MainActivity.kt
```
Flujo de datos: Screen → ViewModel → Repository → DAO → Room, y de vuelta con `Flow` → `StateFlow` → UI.

## Ejecutar
1. Android Studio Koala o superior, JDK 17. `File > Open` y esperar el Gradle Sync.
2. Emulador API 26+ (en *Extended controls > Location* fija una ubicación).
3. Tests unitarios: `./gradlew test`.
