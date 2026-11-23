# Cómo integrar este paquete en el repositorio de Hernando

**Destino:** `https://github.com/Hernando-sys/RentaCuarto`.

Este paquete mejora **únicamente la documentación**. No cambia Kotlin, Room, identificadores de aplicación ni archivos Gradle. Los documentos del proyecto y sus diagramas existentes deben conservarse.

## Pasos seguros

1. En tu copia clonada, verifica `git remote -v` y que apunte a `Hernando-sys/RentaCuarto`.
2. Ejecuta `git status`. Si hay cambios locales, guárdalos o respáldalos antes de continuar.
3. Crea la rama `docs/profesionalizar-repositorio` desde `main` actualizado.
4. Copia los archivos de este ZIP a la **raíz del repositorio**, fusionando la carpeta `docs/` con la actual.
5. No borres `docs/Documento_de_Diseno.md`, `docs/Proyecto_de_Desarrollo_de_Sistema_Movil.md`, los diagramas ni `CONTEXT.md`.
6. Revisa `git diff --stat`, el README y los enlaces relativos.
7. Haz commit y push; abre un Pull Request para revisión.

```bash
git switch main
git pull origin main
git switch -c docs/profesionalizar-repositorio
# Copiar aquí los archivos del paquete a la raíz del clon
git add README.md CONTRIBUTING.md CHANGELOG.md docs/INDICE.md docs/ESTADO_ACTUAL.md docs/REQUERIMIENTOS.md docs/ARQUITECTURA.md docs/MODELO_DE_DATOS.md docs/INSTALACION.md docs/PRUEBAS.md docs/HOJA_DE_RUTA.md
git diff --cached --stat
git commit -m "docs: documentar estado actual, arquitectura y hoja de ruta de RentaCuarto"
git push -u origin docs/profesionalizar-repositorio
```

## Cambios de nombres visibles aún recomendados (no aplicados)

El código todavía conserva rótulos históricos del prototipo. Se recomienda abordar esos cambios en **otro commit `chore: normalizar identidad visual del proyecto`**:

| Ruta | Texto actual | Texto sugerido |
|---|---|---|
| `app/src/main/res/values/strings.xml` | `RentaCuarto EXPO` | `RentaCuarto` |
| `app/src/debug/res/values/strings.xml` | `RentaCuarto Expo DEMO` | `RentaCuarto (Demo)` |
| `ui/inicio/InicioScreen.kt` | `Exposición: pantallas P01 a P12` | `Gestión de propiedades y alquileres` |
| `navigation/AppNavigation.kt` | `RentaCuarto EXPO` / `Gestión de alquileres · Exposición` | `RentaCuarto` / `Gestión de alquileres` |
| `ui/contratos/ContratoDetalleScreen.kt` | Texto que anuncia «versión final» | «La gestión de cobros y garantías está en desarrollo» |

**No renombres aún a ciegas** `applicationId = "com.rentacuarto.expo"` ni `rentacuarto_expo.db`: esos identificadores determinan qué instalación y datos locales utiliza Android. La normalización debe realizarse con pruebas y estrategia de datos.

## Ajustes adicionales de repositorio

- Mejorar `.gitignore` y quitar del seguimiento `.idea/` con `git rm -r --cached .idea` (sin borrar el directorio local).
- Agregar capturas reales con datos ficticios en `docs/CAPTURAS/`; no usar únicamente los wireframes.
- Configurar descripción/topics desde la página del repositorio (requiere permisos de administración del dueño).
- Considerar CI con GitHub Actions cuando la compilación esté verificada en el entorno del equipo.

**Nota:** la conexión GitHub disponible permitió consultar el proyecto pero devolvió 403 al intentar crear una rama. Por eso se entrega un paquete descargable y no se han modificado archivos de `main`.
