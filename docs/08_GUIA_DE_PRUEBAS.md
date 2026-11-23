# 08 · Plan de pruebas y evidencias

Los siguientes casos son **instrucciones reproducibles**, no un reporte de éxito. Registra las pruebas realmente ejecutadas en la tabla final.

## Pruebas de aplicación actual

| ID | Procedimiento | Resultado esperado |
|---|---|---|
| AP-01 | Registrar una propiedad y cerrar/reabrir app | Propiedad persistida en la lista |
| AP-02 | Agregar dos habitaciones con el mismo código a una propiedad | La segunda alta se rechaza |
| AP-03 | Registrar inquilino con DNI de 8 dígitos; repetirlo | Segundo registro rechazado |
| AP-04 | Registrar contrato vigente en habitación libre | Contrato visible; habitación ocupada |
| AP-05 | Intentar otro contrato sin finalizar para la habitación | Alta bloqueada |
| AP-06 | Finalizar contrato con fecha real válida | Contrato finalizado e historial conservado |
| AP-07 | Revisar Inicio después de finalizar | Ocupación y disponibilidad se actualizan |
| AP-08 | Ejecutar procesos AP-01–06 en modo avión | Sin conexión obligatoria |
| AP-09 | Navegar Propiedades → Registrar → Atrás → Inicio | Abre Inicio, no otra sección |

## Pruebas económicas con DAO/repositorio (sin UI todavía)

| ID | Escenario del UML | Resultado esperado |
|---|---|---|
| EC-01 | Generar dos veces la renta 2026-11 para el mismo contrato | Un registro único por contrato-periodo |
| EC-02 | Renta S/500 + cargo S/30 + abonos S/200 y S/330 | Aplicado S/530, saldo cero, PAGADA |
| EC-03 | Abono parcial en dos rentas con distinta antigüedad | Primera renta cubierta antes de la segunda |
| EC-04 | Anular/corregir un abono ya aplicado | Anterior queda anulado; nuevas aplicaciones coherentes |
| EC-05 | Registrar cargo adicional después de pagar | Exigible y aplicaciones se recalculan |
| EC-06 | Garantía pactada S/500, recibida S/300, devolución S/350 | Rechazo por sobreliquidación |
| EC-07 | Aplicar internamente S/100 de garantía | Origen GARANTIA; no aumenta cobros efectivos |
| EC-08 | Cobros efectivos S/1 000, garantía aplicada S/100, gastos S/270 | Resultado mensual S/730 |
| EC-09 | Anular gasto | Se conserva fila; no cuenta en el total |
| EC-10 | Abrir BD v1 con registros y migrar a v2 | 4 tablas conservadas + 6 nuevas; garantía pactada retrocargada |

## Pruebas unitarias (sin emulador)

```bash
./gradlew testDebugUnitTest
```

Se incluyen casos para `CalculosCobros` (distribución FIFO, saldo a favor, estados, resultado), utilidades de fechas/dinero y reglas iniciales.

## Pruebas instrumentadas

```bash
./gradlew connectedDebugAndroidTest
```

Se incluyen casos instrumentados básicos de Room y repositorios. Antes de aprobar entrega, añadir una prueba de migración formal de `MigrationTestHelper` y ejecutar en **API 24** y otra versión superior.

## Metas no funcionales

- **RNF03:** 10 propiedades, 100 habitaciones, 24 meses; panel ≤2 s en al menos 95 % de 30 aperturas (medir p95).
- **RNF04/05:** persistencia tras reinicio y funcionamiento en modo avión.
- **RNF07:** controles principales de ≥48 dp, textos legibles y etiquetas de accesibilidad.
- **RNF08/09:** importes Long en céntimos y fechas ISO sin errores de zona.

## Registro de evidencia

| Fecha | Prueba | Dispositivo/API | Responsable | Resultado observado | Evidencia |
|---|---|---|---|---|---|
| Por completar | AP-01… | — | — | No ejecutada / Pendiente de registrar | — |

**Regla del equipo:** no marque una prueba como aprobada solo porque existe el archivo `.kt`; adjunte captura del emulador, salida del comando o registro de un dispositivo real.
