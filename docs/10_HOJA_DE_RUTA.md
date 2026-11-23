# 10 · Hoja de ruta

El documento UML plantea periodos relativos T1–T10; **no fija fechas calendario definitivas**. Esta hoja de ruta es una secuencia técnica propuesta según dependencias, no evidencia de cumplimiento.

| Hito | Trabajo | Entregable de cierre |
|---|---|---|
| 1 · Consolidación actual | Revisión de navegación, CRUD y contratos; calidad del paquete | Compilación + pruebas de regresión P01–P12 |
| 2 · Persistencia y cálculo | Validar Room v2, migración 1→2, abonos/cargos/gastos/garantías | Tests DAO, transacciones y cálculos económicos |
| 3 · Interfaz de cobros | Rentas, cargos, abonos, estado de cuenta y garantías | Flujos RF08–RF11, RF18, RF21 con pantallas |
| 4 · Panel económico | Deuda vencida, cobros efectivos, gastos y resultado | RF13–RF16 completos y conciliados |
| 5 · Históricos y extensiones | Filtros por fecha, gráficas, alertas configurables, compartir | RF07, RF17, RF19, RF20, RF22 con pruebas |
| 6 · Entrega | Pruebas API 24 + otra versión, uso offline, APK, evidencia y revisión | APK instalable, documentación y acta QA |

**Regla de prioridad:** las funcionalidades nucleares RF01–RF16 se consolidan antes de las extensiones RF17–RF22, salvo dependencias necesarias.

## Riesgos principales

- **Migraciones:** definir esquema y respaldos antes de cambiar tablas.
- **Rentas/abonos:** revisar cargos modificados, pagos parciales y saldo a favor.
- **Garantías:** recepción efectiva, aplicación interna y devoluciones no deben mezclarse con cobros.
- **Trabajo paralelo:** otro integrante debe revisar los PR que cambien contratos o dinero.
- **Entorno:** todos los equipos deben usar versiones compatibles de Android Studio/AGP/Gradle.
