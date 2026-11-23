# 09 · Guía para desarrolladores

## Cómo leer el proyecto

Inicia en `MainActivity.kt`. Sigue `navigation/AppNavigation.kt` y el archivo `Routes.kt`. Elige una pantalla (`ui/*Screen.kt`) y busca su `ViewModel`. Este llama al repositorio correspondiente (`data/repository/*Repository.kt`), que a su vez utiliza el DAO (`data/local/dao/`) para guardar o leer una entidad (`data/local/entity/`).

## Cómo añadir una pantalla futura

1. Confirma el RF del [documento de trazabilidad](06_TRAZABILIDAD_RF.md).
2. Reutiliza entidades y DAO existentes, evitando consultas SQL en los Composables.
3. Crea un ViewModel con `StateFlow`/`Flow`, `viewModelScope` y manejo de excepciones.
4. Crea el Composable con componentes compartidos en `ui/components/`.
5. Añade la ruta y sus parámetros a `Routes.kt` y `AppNavigation.kt`.
6. Prueba validación, navegación, regreso, persistencia y modo avión.
7. Actualiza documentación, pruebas y estado, y envía un Pull Request.

## No modificar la base sin migración

Los datos actuales están en Room v2. Si agregas, renombras o quitas columnas/índices/tablas, sube versión y agrega `Migration(2, 3)`. **Nunca utilices `fallbackToDestructiveMigration()`** para solventar una discrepancia del esquema. Conservar catálogos e historial es parte de los requisitos.

## Estructura actual

```text
app/src/main/java/com/rentacuarto/
├── MainActivity.kt
├── navigation/
│   ├── AppNavigation.kt
│   └── Routes.kt
├── data/
│   ├── local/
│   │   ├── RentaCuartoDatabase.kt
│   │   ├── entity/ (10 archivos)
│   │   └── dao/ (10 archivos)
│   └── repository/
│       ├── PropiedadesRepository.kt
│       ├── InquilinosRepository.kt
│       ├── ContratosRepository.kt
│       ├── CalculosCobros.kt
│       ├── CobrosRepository.kt
│       ├── GastosRepository.kt
│       └── ConsultasRepository.kt
├── ui/
│   ├── inicio/
│   ├── propiedades/
│   ├── contratos/
│   ├── components/
│   └── theme/
└── util/
```

## Convenciones

- Código y nombres de pantalla preferiblemente en español, por coherencia con el dominio.
- `Screen` no conoce SQL. `DAO` no valida reglas del negocio. `Repository` agrupa validaciones y transacciones.
- Los mensajes de error deben ser comprensibles, nunca cadenas técnicas de SQLite en la UI.
- Para dinero usa `Long`, `DineroUtils`, `Math.addExact` y `Math.subtractExact` cuando agregues importes.
- No crees una columna `ocupada` en Habitación ni una tabla independiente de «estado de cuenta» sin motivo claro.
- Separa dinero pactado de recibido. No contabilices un abono de origen `GARANTIA` como ingreso efectivo.
- No subir `.idea/`, `local.properties`, `.db`, claves de firma, documentos con DNI reales ni logs con datos personales.

## Flujo Git

Una rama por tarea (`feat/cobros-pagos`, `fix/ocupacion-futura`, `docs/modelo`), un PR con RF y prueba, revisión cruzada antes de fusionar a `main`. Detalles en [`CONTRIBUTING.md`](../CONTRIBUTING.md).
