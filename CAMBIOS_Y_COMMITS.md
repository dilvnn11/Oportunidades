# Cambios incluidos en esta versión y commits sugeridos

Este ZIP es el proyecto completo con estos cambios ya aplicados. No se modificó ninguna lógica: solo se reordenaron archivos, se agregaron pruebas y se corrigió el README.

## Qué cambió
1. `ui/oportunidad/OportunidadUiState.kt` (nuevo): el `OportunidadUiState` ahora tiene su propio archivo. Se quitó de `OportunidadViewModel.kt`. Mismo paquete, no cambian imports.
2. `ui/auth/AuthUiState.kt` (nuevo): lo mismo para `AuthUiState`, que estaba dentro de `AuthViewModel.kt`.
3. `datos/local/UsuarioEntity.kt` y `datos/local/UsuarioDao.kt` (nuevos): se separaron de `OportunidadEntity.kt` y `OportunidadDao.kt`. Misma tabla y mismas consultas, así que la base de datos no cambia (sigue `version = 1`).
4. `ValidadoresOportunidadTest.kt` y `ValidadoresAuthTest.kt` (nuevos): 38 pruebas unitarias nuevas de las reglas de validación. `ValidadoresTest.kt` se deja tal cual.
5. `README.md`: enlace público de Trello, aclaración A = Ariel y B = Deland, estructura actualizada, sección de recursos nativos y de pruebas.

## Lo que NO está incluido (hay que hacerlo en el repo)
- `gradlew`, `gradlew.bat` y `gradle/wrapper/gradle-wrapper.jar`: el repo no los tiene, y sin ellos `./gradlew test` no funciona. Cópienlos de otro proyecto Android Studio que use Gradle 8.9 (misma versión que `gradle-wrapper.properties`).

## Commits sugeridos (que cada uno haga los suyos desde su cuenta, con git add / commit / push)
Ariel (Integrante A):
- `refactor: mover OportunidadUiState a su propio archivo`
- `refactor: separar UsuarioEntity y UsuarioDao de las clases de oportunidad`
- `chore: agregar gradle wrapper para ejecutar tests por consola`

Deland (Integrante B):
- `refactor: mover AuthUiState a su propio archivo`
- `test: agregar pruebas unitarias de validadores de oportunidad`
- `test: agregar pruebas unitarias de validadores de autenticación`
- `docs: actualizar README con recursos nativos, pruebas y enlace de Trello`

Solo hagan estos commits si de verdad aplican estos cambios en sus equipos. Después de aplicarlos: abrir el proyecto en Android Studio, esperar el Gradle Sync, ejecutar los tests y probar la app una vez antes de subir.

## Recordatorio
Cualquier cambio en el repositorio después de la fecha de entrega y antes de la evaluación deja la nota en 1.0. Hagan todo antes de entregar.
