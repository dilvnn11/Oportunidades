# Gestión móvil de oportunidades y activaciones corporativas

**Caso:** DSY1105_007D  
**Evaluaciones:** Parcial 4 (Entrega) y Parcial 5 (Defensa Individual)  
**Integrantes:** Ariel Catalan (Integrante A en Trello) · Deland Cabello (Integrante B en Trello)  
**Equipo N°:** 3  
**Trello:** https://trello.com/b/6ad6455041149ef82eb3bd48  
**Repositorio GitHub:** https://github.com/dilvnn11/Oportunidades  

---

## 1. Nombre del Proyecto y Contexto
**Gestión móvil de oportunidades y activaciones corporativas:** Sistema móvil nativo en Android (Kotlin + Jetpack Compose) integrado a un microservicio propio en Spring Boot y a la API pública de indicadores económicos, diseñado para que ejecutivos de ventas y corporativos capturen, valoren y gestionen oportunidades de negocio con evidencia multimedia y geolocalización.

---

## 2. Integrantes y Responsabilidades (Commits por Integrante)
- **Ariel Catalan (Integrante A):**
  - Desarrollo del Microservicio Spring Boot (`backend/`).
  - Configuración de firma digital y llaves (`release-key.jks`, `app/build.gradle.kts`).
  - Implementación de `OportunidadUiState` y separación de datos locales (`UsuarioEntity` / `UsuarioDao`).
  - Generación del ejecutable APK firmado (`apk/app-release.apk`).
- **Deland Cabello (Integrante B):**
  - Configuración de Retrofit e integración con API externa de mercado (`mindicador.cl`).
  - Implementación de pruebas unitarias (`OportunidadViewModelTest`, `OportunidadRepositoryTest`, `ValidadoresTest`).
  - Refactorización de la capa `AuthUiState` y actualización de la documentación/Trello.

---

## 3. Funcionalidades Implementadas

### App Móvil (Kotlin + Jetpack Compose)
- **Autenticación:** Registro e inicio de sesión con almacenamiento local seguro en Room y encriptación SHA-256 de contraseñas.
- **Lista Reactiva de Oportunidades:** Visualización de tarjetas animadas por prioridad, estado y filtros de búsqueda (`ListaOportunidadesScreen`).
- **Formulario Completo:** Creación y modificación con nombre, tipo, descripción, fuente, prioridad, potencial comercial, estado y observaciones (`OportunidadFormScreen`).
- **Integración con API Externa:** Consulta en tiempo real de valores UF, Dólar y Euro desde `https://mindicador.cl/api` para la valoración comercial.
- **Recursos Nativos:**
  - **Cámara:** Captura de evidencia fotográfica (`TakePicturePreview`) guardada en almacenamiento interno.
  - **Geolocalización:** Obtención de latitud y longitud con permiso en tiempo de ejecución (`ACCESS_FINE_LOCATION`).
- **Animaciones UI:** Transiciones entre pantallas, cambio dinámico de color de prioridad y avisos reactivos de estado.

---

## 4. Endpoints Utilizados

### A. Microservicio Propio (Spring Boot) - Base URL: `http://localhost:8080/` o `http://10.0.2.2:8080/`
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api/oportunidades` | Obtener listado completo de oportunidades almacenadas en el servidor |
| `GET` | `/api/oportunidades/{id}` | Obtener el detalle de una oportunidad específica por ID |
| `POST` | `/api/oportunidades` | Crear una nueva oportunidad en la base de datos remota |
| `PUT` | `/api/oportunidades/{id}` | Actualizar una oportunidad existente |
| `DELETE` | `/api/oportunidades/{id}` | Eliminar una oportunidad |

### B. API Externa Pública (Mindicador) - Base URL: `https://mindicador.cl/`
| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| `GET` | `/api` | Obtener valores actualizados de UF, Dólar y Euro en tiempo real |

---

## 5. Pasos para Ejecutar

### Step 1: Iniciar el Microservicio Spring Boot (`backend/`)
1. Abrir una consola en la carpeta del backend: `cd backend`.
2. Ejecutar el servidor Spring Boot:
   ```bash
   ./gradlew bootRun
   ```
   *(El microservicio se iniciará en `http://localhost:8080` con base de datos H2 activa).*

### Step 2: Ejecutar la Aplicación Android (`app/`)
1. Abrir el proyecto en Android Studio Koala o superior (JDK 17).
2. Sincronizar Gradle (*Sync Project with Gradle Files*).
3. Iniciar un Emulador Android (API 26+) o conectar un dispositivo físico.
4. Ejecutar la aplicación pulsando **Run 'app'** (`Shift + F10`).

---

## 6. Firma y Generación del APK Executable

El ejecutable firmado digitalmente y su configuración asociada se encuentran disponibles en el repositorio:
- **Archivo de Llave JKS:** `release-key.jks` (alias: `oportunidadeskey`, password: `password123`)
- **APK Firmado:** `apk/app-release.apk`
- **Comando de Generación:**
  ```bash
  ./gradlew assembleRelease
  ```

---

## 7. Pruebas Unitarias (Cobertura > 80%)
Se han implementado pruebas unitarias exhaustivas con JUnit4, MockK y Kotlin Coroutines Test:
- `OportunidadViewModelTest.kt`: Pruebas del flujo de estados, validaciones y llamadas al repositorio mockeadas.
- `OportunidadRepositoryTest.kt`: Pruebas de la capa de datos local y remota (DAO, Retrofit y API externa).
- `ValidadoresAuthTest.kt` y `ValidadoresOportunidadTest.kt`: Pruebas de reglas de negocio puras.

Para ejecutar todas las pruebas por consola:
```bash
./gradlew test
```

---

## 🎓 8. Guía de Defensa Técnica Individual (Evaluación Parcial 5)

Esta sección contiene la preparación paso a paso para la defensa individual presencial (15 min):

1. **Entorno y Ejecución:**
   - Demostrar que Spring Boot corre en el puerto `8080` y que el emulador Android se conecta mediante `10.0.2.2:8080`.
2. **Arquitectura MVVM y Paquetes:**
   - Explicar separación de capas: `datos` (local/remote), `dominio` (reglas de negocio sin Android), y `ui` (Compose + ViewModel + UiState).
3. **Microservicios Creados:**
   - Mostrar el `OportunidadController` en Spring Boot y probar endpoints en directo o mediante la app.
4. **Consumo de API Externa:**
   - Explicar `ExternalApiService` consumiendo `mindicador.cl` vía Retrofit y cómo se muestra en la `ListaOportunidadesScreen`.
5. **Persistencia Remota y CRUD:**
   - Crear, editar o eliminar una oportunidad desde la app y verificar el cambio reactivo en la UI y en la BD.
6. **Pruebas Unitarias:**
   - Ejecutar `./gradlew test` en tiempo real y explicar qué se mockea (`mockk`, `coEvery`, `coVerify`).
7. **Firma del APK:**
   - Explicar el comando `keytool` utilizado y el bloque `signingConfigs` en `app/build.gradle.kts`.
8. **Colaboración GitHub & Trello:**
   - Mostrar el historial de commits por integrante y el tablero de Trello con el flujo de tareas.
