# Integrar este proyecto en Hernando-sys/RentaCuarto

Este ZIP es un **proyecto Android Studio completo**, preparado a partir del avance funcional del repositorio revisado y del UML profesional adjunto. No contiene la carpeta oculta `.git`: **no elimina el historial del repositorio** si copias su contenido dentro de tu clon actual. La capa de datos económica está programada, pero las pantallas de Cobros, Gastos e Informes están pendientes.

## Precauciones antes de copiar

1. Realiza copia de seguridad de los datos reales ingresados en emuladores/teléfonos antes de cambiar `applicationId` o nombres de bases de datos.
2. Revisa que `git status` esté limpio o guarda tus cambios actuales en un commit o una rama.
3. La referencia de los requisitos será `docs/referencias/RentaCuarto_Proyecto_UML_Profesional.docx` (**22 RF, no 23**).
4. No copies `.git` desde otra carpeta. **Este ZIP no tiene `.git`.**
5. No publiques DNI, teléfonos ni capturas reales de inquilinos.

## Procedimiento recomendado

Clona o abre el repositorio que ya tienes:

```bash
git clone https://github.com/Hernando-sys/RentaCuarto.git
cd RentaCuarto
git status
git switch main
git pull origin main
git switch -c feat/integrar-capa-datos-documentacion
```

**Antes de copiar el contenido del ZIP**, archiva los materiales de diseño anteriores (el documento anterior menciona 23 RF y no coincide con el UML profesional de 22 RF):

```bash
mkdir -p docs/referencias
# Solo si existe en tu clon:
git mv docs/Documento_de_Diseno.md docs/referencias/Documento_de_Diseno_Anterior.md
# Solo si la carpeta existe, conserva sus diagramas anteriores:
git mv docs/diagramas docs/diagramas_anteriores
```

Copia **el contenido interno de `RentaCuarto_Entrega_Profesional/`** sobre la raíz de tu clon (en la carpeta donde está `settings.gradle.kts`). Autoriza sobrescribir los archivos de código y configuración que correspondan. No dejes una subcarpeta `RentaCuarto_Entrega_Profesional` dentro del repositorio.

Retira del control de versiones archivos locales antiguos (si existían):

```bash
# Si estaban versionados en el repo previo:
git rm -r --cached .idea
# Si siguen presentes en el clon (plantillas que no pertenecen al proyecto):
git rm -r app/src/main/java/com/example app/src/test/java/com/example app/src/androidTest/java/com/example
# Se retira el archivo de configuración que forzaba un JDK distinto en cada máquina:
git rm gradle/gradle-daemon-jvm.properties
```

Si alguno de estos comandos indica **pathspec not found**, significa que ese archivo ya no está versionado; puedes continuar. En Windows PowerShell, `mkdir -p` no se usa exactamente igual; crea la carpeta desde el Explorador o ejecuta `New-Item -ItemType Directory -Force docs/referencias`.

Antes de subir:

```bash
git status
git add -A
git diff --cached --stat
# Ejecutar desde Android Studio o terminal compatible:
# .\gradlew.bat testDebugUnitTest
# .\gradlew.bat assembleDebug

git commit -m "feat: integrar Room completo y documentacion del proyecto"
git push -u origin feat/integrar-capa-datos-documentacion
```

Crea un Pull Request hacia `main` y solicita revisión del equipo. **No se ha hecho ningún push desde este ZIP.**

## Cambio de identidad de la aplicación

Antes: `com.rentacuarto.expo` + `rentacuarto_expo.db`.
Ahora: `com.rentacuarto` + `rentacuarto.db` (**Debug `com.rentacuarto.debug`**).

Estas instalaciones tienen almacenamientos Android independientes: **no hay migración automática entre sus datos**. `MIGRACION_1_2` sirve para una base v1→v2 dentro de la misma instalación/archivo; no equivale a transferir información entre aplicaciones con distinto ID.

## Qué podrás presentar como implementado

- Pantallas actuales: Inicio, Alertas contractuales, Propiedades, Habitaciones, Inquilinos y Contratos.
- Persistencia Room: diez entidades y diez DAO, migración incremental y repositorios económicos preparados.
- Cálculos puros: distribución FIFO de abonos, estados de renta, saldo y resultado.
- Documentación profesional en Markdown, UML de referencia y 18 figuras.

**No presentar como ya funcional en la UI:** abonos, estado de cuenta, liquidación de garantías, gastos, gráficos o reportes históricos. Están preparados en datos parcial o totalmente, pero sus pantallas y pruebas E2E no están concluidas.

## Antes de considerar el PR aprobado

- Sincronizar Gradle en Android Studio compatible con AGP 9.5.2 y SDK API 37.
- Compilar Debug/Release y ejecutar los tests en un emulador y un teléfono cuando sea posible.
- Probar alta de propiedad, habitaciones, inquilino, contrato y finalización.
- Probar una migración Room de la versión anterior con **datos respaldados**.
- Comprobar que el `.gitignore` excluye `.idea`, datos locales y claves.
- Verificar que las reglas de rentas/abonos/garantías no alteren la navegación actual.
