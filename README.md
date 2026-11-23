# RentaCuarto – Exposición P01 a P12

**Proyecto Android Studio independiente** derivado de RentaCuarto V4. Solo contiene las pantallas P01–P12 y las tres pantallas de edición indispensables para el CRUD. Mantiene los estilos de Jetpack Compose de V4.

## Cómo abrir
1. Descomprime el ZIP y abre **esta carpeta** (la que contiene `settings.gradle.kts`) con Android Studio → Open.
2. Espera el Gradle Sync. Esta copia conserva las versiones de Gradle/AGP de la V4 original, **AGP 9.5.2**. Android Studio debe ser compatible.
3. Selecciona módulo `app`, variante **debug** y un emulador; pulsa Run.
4. Se instala **RentaCuarto Expo DEMO**, con identificador `com.rentacuarto.expo.demo`, distinto de la app del proyecto principal.
5. En el primer inicio la demo inserta dos propiedades, seis habitaciones, cinco inquilinos y cuatro contratos ficticios.

## Alcance
| Pantalla | Nombre | Archivo |
|---|---|---|
| P01 | Inicio (solo ocupación y propiedades) | `ui/inicio/InicioScreen.kt` |
| P02 | Alertas de fin de contrato | `ui/inicio/AlertasScreen.kt` |
| P03 | Propiedades | `ui/propiedades/PropiedadesScreen.kt` |
| P04 | Registro de propiedad | `ui/propiedades/PropiedadFormScreen.kt` |
| P05 | Detalle de propiedad y habitaciones | `ui/propiedades/PropiedadDetalleScreen.kt` |
| P06 | Registro de habitación | `ui/propiedades/HabitacionFormScreen.kt` |
| P07 | Inquilinos | `ui/contratos/InquilinosScreen.kt` |
| P08 | Registro de inquilino | `ui/contratos/InquilinoFormScreen.kt` |
| P09 | Contratos | `ui/contratos/ContratosScreen.kt` |
| P10 | Registro de contrato | `ui/contratos/ContratoFormScreen.kt` |
| P11 | Detalle de contrato | `ui/contratos/ContratoDetalleScreen.kt` |
| P12 | Finalizar contrato | `ui/contratos/FinalizarContratoScreen.kt` |

También están `ActualizarPropiedadScreen.kt`, `ActualizarHabitacionScreen.kt` y `ActualizarInquilinoScreen.kt`.

## Base de datos mínima
Cuatro tablas Room: `propiedades`, `habitaciones`, `inquilinos` y `contratos`. El proyecto usa otra base, `rentacuarto_expo.db`, y **otro applicationId**. Por eso no modifica el proyecto completo ni sus datos.

El proyecto V4 completo sigue siendo la referencia para P13–P20. Esta versión no incluye el módulo de Cobros, Gastos, garantías recibidas ni reportes económicos. La garantía en P10/P11 es **solo una condición pactada**, no un pago real.

## Datos de muestra
`app/src/debug/java/com/rentacuarto/demo/DatosExpo.kt` contiene la carga ficticia. Se ejecuta una sola vez sobre una base vacía; en reinicios posteriores conserva las modificaciones del usuario. Para reiniciar la muestra: Ajustes del teléfono → Aplicaciones → RentaCuarto Expo DEMO → Almacenamiento → Borrar datos. **No uses ese comando sobre la aplicación principal.**

## Pruebas
Consulta `docs/PRUEBAS_P01_P12.md`. No se ha compilado con el SDK real ni ejecutado en un emulador en este entorno; ejecuta Sync, Build y pruebas antes de exponer.
