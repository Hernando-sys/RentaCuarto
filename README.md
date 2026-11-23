# RentaCuarto

**Sistema móvil para la gestión de habitaciones en alquiler, rentas y control económico de propiedades residenciales.**

Proyecto académico de **Desarrollo de Sistemas Móviles** · UNMSM, Facultad de Ingeniería de Sistemas e Informática · Grupo 10 · 2026.

![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?logo=kotlin&logoColor=white) ![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4) ![Room](https://img.shields.io/badge/Persistencia-Room%20%2F%20SQLite-0B766C) ![Estado](https://img.shields.io/badge/Estado-En%20desarrollo-1D4ED8)

## ¿Qué problema resuelve?

Los propietarios de casas y pequeños edificios suelen controlar habitaciones, inquilinos y contratos con cuadernos, mensajes o hojas dispersas. **RentaCuarto** centraliza la información en un teléfono Android, calcula la ocupación desde los contratos y está diseñado para incorporar cobros, pagos, depósitos en garantía, gastos y consultas económicas.

El propietario/administrador es el único usuario de la aplicación. **No requiere servidor, cuenta de usuario ni conexión permanente a Internet.** La información se almacena localmente con Room/SQLite.

## Estado del desarrollo

El código publicado permite navegar por **Inicio, Alertas, Propiedades, Habitaciones, Inquilinos y Contratos**. Incluye registro, consulta y edición de catálogos, alta y finalización contractual y consultas de ocupación. Las pantallas principales de cobros, gastos y reportes todavía **no forman parte de la interfaz**.

La capa de persistencia está **preparada con diez entidades y diez DAO**; se agregaron repositorios económicos (rentas, cargos, abonos, garantías y gastos) y una migración de Room **v1→v2**. Estas nuevas operaciones están **programadas, pero requieren compilación y pruebas integrales en Android** antes de declararse validadas.

| Dominio | Disponible en interfaz | Persistencia / reglas | Estado |
|---|---|---|---|
| Propiedades y habitaciones | Sí | Sí | Avance funcional; falta desactivación lógica y fecha de consulta configurable |
| Inquilinos y contratos | Sí | Sí | Avance funcional; falta completar historial en UI y alertas de deuda |
| Ocupación y alertas contractuales | Sí | Sí | Fecha actual y alertas con umbral fijo |
| Rentas, cargos y abonos | No | Preparada | Integración y pruebas de extremo a extremo pendientes |
| Depósitos en garantía | No | Preparada | Recepción/liquidación pendiente de integración y auditoría completa |
| Gastos y resultado económico | No | Preparada | Integración y pruebas pendientes |
| Informes históricos y comparativos | No | Parcial | Pendiente |

**Fuente de alcance:** [Documento UML de RentaCuarto](docs/referencias/RentaCuarto_Proyecto_UML_Profesional.docx) (22 RF; 16 RNF; 10 entidades). [Matriz de requisitos y estado](docs/06_TRAZABILIDAD_RF.md).

## Inicio rápido

1. Instala Android Studio compatible con el **Android Gradle Plugin** y el SDK declarado en `app/build.gradle.kts`. Revisa [Instalación](docs/02_INSTALACION.md).
2. Clona el repositorio: `git clone https://github.com/Hernando-sys/RentaCuarto.git`.
3. Abre la carpeta que contiene `settings.gradle.kts` (no una carpeta exterior adicional).
4. Sincroniza Gradle; elige la configuración `app` y un emulador Android (API 24 o superior).
5. Ejecuta la variante **debug**, que carga registros **ficticios** en su propio almacenamiento. La variante **release** no contiene el cargador de prueba. El identificador de la app es `com.rentacuarto` (`com.rentacuarto.debug` en Debug); por lo tanto, no reemplaza ni importa datos de instalaciones antiguas de `com.rentacuarto.expo`.

Para ejecutar los tests después de sincronizar (los comandos por sí solos no demuestran resultados aprobados):

```bash
# Windows
.\gradlew.bat testDebugUnitTest
.\gradlew.bat connectedDebugAndroidTest

# macOS / Linux
./gradlew testDebugUnitTest
./gradlew connectedDebugAndroidTest
```

Los comandos son instrucciones de ejecución, **no resultados de pruebas ya aprobadas**.

## Estructura del proyecto

```text
RentaCuarto/
├── app/src/main/java/com/rentacuarto/
│   ├── MainActivity.kt         # Punto de entrada
│   ├── navigation/             # Rutas y NavHost
│   ├── ui/                     # Pantallas, ViewModels y diseño
│   ├── data/local/             # Room: 10 Entity, 10 DAO y migración
│   ├── data/repository/        # Reglas y transacciones de negocio
│   └── util/                   # Conversión de dinero y fechas
├── app/src/debug/             # Datos inventados, solo desarrollo
├── app/src/test/              # Pruebas locales
├── app/src/androidTest/       # Pruebas en emulador/dispositivo
├── docs/                      # Diseño, arquitectura, requisitos y evidencia
├── CONTRIBUTING.md            # Normas del equipo
├── CHANGELOG.md               # Historial de entregas
└── settings.gradle.kts        # Configuración Gradle
```

**Flujo básico:** `Pantalla Compose → ViewModel → Repository → DAO → Room/SQLite`.

## Documentación

Consulta el [índice completo de la documentación](docs/INDICE.md). Para entender las reglas, empieza por [Arquitectura](docs/03_ARQUITECTURA.md), [Modelo de datos](docs/04_MODELO_DE_DATOS.md) y [Reglas de negocio](docs/05_REGLAS_DE_NEGOCIO.md).

Para saber exactamente qué falta, revisa el [estado y pendientes](docs/07_ESTADO_Y_PENDIENTES.md) y la [hoja de ruta](docs/10_HOJA_DE_RUTA.md).

## Equipo

Grupo 10: JeanCarlos Huanacuni Gomez, Álvaro Jesús Quispe Ñahui, Kevin Hernando Quispe Carbajal, Marco Alejandro Tafur Ari y Rodrigo Contreras Peña. La distribución por módulos está en [Organización del equipo](docs/11_EQUIPO_Y_GESTION.md).

> **Seguridad y datos:** los nombres, DNI y teléfonos incluidos en el modo Debug son ficticios. No publiques registros de inquilinos reales, bases `.db`, contraseñas ni archivos de firma. El alcance es académico; no se afirma que existan nube, sincronización o respaldo externo implementados.
