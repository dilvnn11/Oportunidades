# Gestión Móvil de Oportunidades y Activaciones Corporativas - OpYCorp (Caso 007D)

**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105_007D)  
**Evaluación:** Evaluación Parcial 4 (Entrega) / Evaluación Parcial 5 (Defensa Técnica Individual)  
**Institución:** Duoc UC – Escuela de Informática y Telecomunicaciones  
**Equipo N°:** 3  
**Integrantes:**
- **Ariel Catalan** (Integrante A - Lógica de datos, Microservicio Spring Boot, Firma APK)
- **Deland Cabello** (Integrante B - Retrofit, API Externa, Pruebas Unitarias, Documentación)

**Tablero Trello:** [Ver Tablero de Trello](https://trello.com/b/6ad6455041149ef82eb3bd48)  
**Repositorio GitHub:** [https://github.com/dilvnn11/Oportunidades](https://github.com/dilvnn11/Oportunidades)  

---

## 1. Contexto del Proyecto

**OpYCorp** es una solución móvil nativa en Android (Kotlin + Jetpack Compose) conectada a un microservicio independiente en Spring Boot y a la API pública de indicadores económicos (`https://mindicador.cl/api`).

La aplicación está diseñada para que ejecutivos comerciales y gestores de activaciones corporativas registren, evalúen, valoren económicamente y geolocalicen oportunidades de negocio con captura multimedia (cámara) y lectura de códigos QR.

---

## 2. Arquitectura del Sistema (MVVM + Microservicios + Offline-First)

```text
├── app/                        # Aplicación Android Móvil
│   ├── datos/
│   │   ├── local/              # Room Database (OportunidadEntity, UsuarioEntity, DAOs)
│   │   ├── remote/             # Retrofit Clients, ApiServices y DTOs
│   │   └── repository/         # Repositorios híbridos (Local + Remoto)
│   ├── dominio/                # Reglas de validación puras y Opciones
│   └── ui/                     # Jetpack Compose UI (Screens, ViewModels, UiState)
├── backend/                    # Microservicio Spring Boot (Java 17, JPA, H2)
│   ├── src/main/java/cl/duoc/backend/
│   │   ├── controller/         # OportunidadController (Endpoints REST)
│   │   ├── model/              # OportunidadModel (Entidad JPA)
│   │   └── repository/         # OportunidadSpringRepository
│   └── src/main/resources/application.properties
├── release-key.jks             # Clave digital para firma de producción
└── apk/app-release.apk         # APK Firmado ejecutable
```

---

## 3. Funcionalidades Clave Implementadas

1. **Gestión Completa de Oportunidades (CRUD Híbrido):**
   - Lista reactiva con tarjetas animadas según prioridad y estado.
   - Formulario de alta y edición con validaciones visuales instantáneas en cada campo.
   - Sincronización en tiempo real entre la base de datos local (Room) y el microservicio remoto.
2. **Consumo de API Externa (Mindicador):**
   - Consulta de valores UF, Dólar y Euro para la valoración comercial de oportunidades.
3. **Integración con Recursos Nativos del Dispositivo:**
   - **Cámara Nactiva:** Captura de evidencia fotográfica almacenada en memoria interna.
   - **Lector QR (ZXing):** Escaneo directo de códigos QR para completar automáticamente el origen o fuente de la oportunidad.
   - **GPS / Geolocalización:** Obtención de latitud y longitud con permisos en tiempo de ejecución.
4. **Pruebas Unitarias Exhaustivas (>80% Cobertura):**
   - Módulo de tests unitarios para `ViewModel` y `Repository` con `JUnit4`, `MockK` y `Kotlin Coroutines Test` (51 tests pasados).
5. **Generación de Ejecutable Firmado (Release APK):**
   - Configuración de firma digital `release-key.jks` en `app/build.gradle.kts`.

---

## 4. Endpoints y Microservicios

### Microservicio Propio (Spring Boot) - Base URL: `http://localhost:8080/`
- `GET /api/oportunidades`: Obtener lista completa de oportunidades.
- `GET /api/oportunidades/{id}`: Obtener detalle de oportunidad por ID.
- `POST /api/oportunidades`: Crear nueva oportunidad.
- `PUT /api/oportunidades/{id}`: Actualizar oportunidad existente.
- `DELETE /api/oportunidades/{id}`: Eliminar oportunidad.

### API Externa Pública (Mindicador) - Base URL: `https://mindicador.cl/`
- `GET /api`: Obtener valores económicos actualizados (UF, Dólar, Euro, UTM).

---

## 5. Instrucciones de Ejecución

### Pasos para el Microservicio Backend:
1. Abrir terminal en la carpeta del backend: `cd backend`.
2. Ejecutar: `./gradlew bootRun` (Inicia Spring Boot en puerto 8080 con base de datos H2).

### Pasos para la Aplicación Android:
1. Abrir la carpeta raíz en Android Studio Koala o superior (JDK 17).
2. Sincronizar Gradle (*Sync Project with Gradle Files*).
3. Conectar un dispositivo físico o emulador (API 26+).
4. Presionar **Run 'app'** (`Shift + F10`).
5. Para correr las pruebas unitarias:
   ```bash
   ./gradlew test
   ```

---

## 6. Firma Digital y APK

- **Llave JKS:** `release-key.jks`
- **Alias:** `oportunidadeskey` | **Password:** `password123`
- **Ubicación del APK:** `apk/app-release.apk`

---

## 🎓 7. Guía de Defensa Técnica Individual (Evaluación Parcial 5)

| Paso | Explicación / Demostración en Vivo |
| :--- | :--- |
| **1. Entorno** | Mostrar Spring Boot corriendo en `:8080` y la app en el emulador (`10.0.2.2:8080`). |
| **2. Arquitectura** | Explicar capas MVVM (`datos`, `dominio`, `ui`) y paquete `backend/`. |
| **3. Microservicio** | Mostrar `OportunidadController` y probar creación/edición de datos. |
| **4. API Externa** | Explicar `ExternalApiService` consumiendo valores UF/Dólar en Retrofit. |
| **5. Persistencia** | Demostrar flujo offline/online con Room y Spring Boot JPA. |
| **6. Tests** | Correr `./gradlew test` (51/51 pasados) y explicar mocks con MockK. |
| **7. Firma APK** | Mostrar `release-key.jks` y bloque `signingConfigs` en Gradle. |
| **8. Colaboración** | Evidenciar commits por integrante en GitHub y tarjetas en Trello. |
| **9. Live Coding** | Ajustar un endpoint o test según indicación del docente. |
