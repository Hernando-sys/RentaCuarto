# 02 · Instalación y configuración

## Requisitos

- Windows, Linux o macOS con **Android Studio compatible con AGP 9.5.2**.
- Android SDK **API 37** disponible para `compileSdk` (declarado en el proyecto).
- JDK adecuado para la versión de Gradle y AGP instalada. En Android Studio selecciona **Gradle JDK** compatible; se eliminó el archivo local que obligaba a descargar Java 25 en cada equipo.
- Emulador o teléfono con **Android 7.0 (API 24) o superior**.
- Internet al sincronizar por primera vez, para descargar dependencias. **La app funciona offline** tras instalarse.

> Si Android Studio muestra `AGP 9.5.2 is incompatible`, el problema es de compatibilidad del IDE y no necesariamente del código Kotlin. Actualiza Android Studio o acuerda una matriz de versiones con el equipo antes de cambiar AGP.

## Abrir el proyecto

```bash
git clone https://github.com/Hernando-sys/RentaCuarto.git
```

En Android Studio: **File → Open** → selecciona directamente la carpeta con `settings.gradle.kts` y `app/` → **Sync Project with Gradle Files** → selecciona módulo `app` → **Run**.

Si el código está dentro de dos carpetas `RentaCuarto/RentaCuarto`, abre **la interior que contiene `settings.gradle.kts`**.

## Configuración técnica

- `gradle/libs.versions.toml`: versiones y alias de dependencias.
- `app/build.gradle.kts`: configuración Android, SDK, KSP, Compose, desugaring, pruebas.
- `settings.gradle.kts`: repositorios Google/Maven y módulo `:app`.
- `gradle/wrapper/`: versión consistente del ejecutor Gradle.
- `app/src/main/AndroidManifest.xml`: entrada de Android.

`applicationId` de la aplicación normal: **`com.rentacuarto`**. La variante `debug` usa **`com.rentacuarto.debug`** y se instala por separado. La fuente Debug inserta datos ficticios en una base local solo si está vacía; no se incluye en la variante `release`.

**Atención a instalaciones anteriores:** el proyecto experimental utilizó otro `applicationId` (`com.rentacuarto.expo`) y base `rentacuarto_expo.db`. Android considera cada `applicationId` una aplicación distinta: **no transfiere automáticamente sus datos** al nuevo paquete. Haz una exportación respaldada antes de cambiar de instalación si ingresaste registros propios.

## Ejecutar pruebas

```bash
# Pruebas unitarias locales
./gradlew testDebugUnitTest
# Pruebas instrumentadas: emulador encendido
./gradlew connectedDebugAndroidTest
# Compilar APK de depuración
./gradlew assembleDebug
```

En Windows usa `.\gradlew.bat` en lugar de `./gradlew`.

## Resolución rápida de problemas

| Síntoma | Acción |
|---|---|
| `Module not specified` | Abrir la carpeta correcta, sincronizar y seleccionar `app`. |
| AGP no compatible | Actualizar el IDE compatible o coordinar el downgrade de toda la cadena de dependencias. |
| `SDK location not found` | Configurar Android SDK en Android Studio; no subir `local.properties`. |
| Error de KSP/Room | Revisar `libs.versions.toml`, sincronizar y comprobar versiones Kotlin/KSP. |
| Error de migración Room | No borrar datos a ciegas: consultar Logcat, respaldar DB y revisar esquema v1/v2. |
| No aparecen datos ficticios | Instalar **debug**; verificar que la base de esa app esté vacía y revisar Logcat. |

La compilación completa y los resultados en dispositivos **deben registrarse** en [Pruebas](08_GUIA_DE_PRUEBAS.md); no se asume que hayan pasado por el simple hecho de existir el código.
