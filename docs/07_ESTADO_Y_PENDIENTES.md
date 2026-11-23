# 07 · Estado del proyecto y pendientes técnicos

**Corte técnico:** commit `10fdd162` de `main` (23/11/2026), seguido por mantenimiento del repositorio para normalizar configuración, pruebas y archivos versionados. La capa Room económica está programada; la compilación Android y las pruebas instrumentadas requieren ejecución real del equipo.

## Implementado en la interfaz

- Pantallas del flujo **Inicio → Propiedades → Habitaciones → Inquilinos → Contratos → Finalización**.
- Listados, formularios, ediciones, búsqueda, estado de ocupación actual y avisos de fin pactado próximo.
- Base Room inicial con contratos y catálogos, pantalla de detalle y navegación básica.
- Variante Debug con información ficticia, separada de Release por `applicationIdSuffix`.

## Preparado en la capa de datos (pendiente de integración visual)

- Diez entidades, diez DAO, FK/índices y migración Room 1→2.
- Rentas únicas por contrato y periodo; base histórica.
- Abonos efectivos, corrección por anulación lógica y nueva fila, distribución a las rentas más antiguas.
- Cargos por servicio y recálculo de aplicaciones.
- Depósito pactado, recepción efectiva y liquidación con verificación del recibido.
- Gastos por propiedad y cálculo del resultado mensual excluyendo aplicaciones internas.
- Consultas económicas básicas de estado de cuenta cronológico, deuda vencida a una fecha y cobros efectivos; requieren QA histórica y UI.

## Pendientes prioritarios

| Prioridad | Trabajo pendiente | Motivo |
|---|---|---|
| Alta | Compilar y ejecutar en Android real (SDK/API compatibles) | No existe evidencia reproducible de este paquete |
| Alta | Probar migración 1→2 con datos existentes y sin eliminarlos | Integridad de datos |
| Alta | Validar contratos futuros y no solapamiento por intervalos | RF03 / RF05 |
| Alta | Agregar UI P13–P17 para garantía, renta, abonos, estado de cuenta y cargos | Flujo económico visible |
| Alta | Completar estado de cuenta cronológico con saldo acumulado | RF11 |
| Alta | Añadir gastos y resultado mensual en UI | RF15 / RF16 |
| Media | Historial contractual navegable y búsquedas completas | RF04 / RF07 |
| Media | Desactivación lógica de propiedades | RF01, preservar historial |
| Media | Alerta de deuda/garantía al finalizar | RF06 |
| Media | Consulta por fecha y umbrales editables | RF03, RF12, RF13, RF17 |
| Media | Auditoría temporal de recepciones/devoluciones de garantía | Trazabilidad financiera |
| Media | Pruebas de unidad, DAO y E2E sobre dos Android | RNF13 |
| Media | Compartir constancia y estado de cuenta | RF19 |
| Baja | Evolución histórica y gráficos comparativos | RF20 / RF22 |

## Notas de calidad

- El código ejecutable conserva pantallas sin cobros; **no hay interfaz de pagos operativa**.
- `CalculosCobros.kt` contiene cálculos puros, aptos para pruebas unitarias. Las operaciones transaccionales de Room requieren pruebas instrumentadas.
- `GarantiaEntity` representa importes acumulados; **no sustituye** una bitácora detallada de cada recepción en fecha/medio.
- El repositorio no tiene backend ni sincronización de dispositivos. No se debe prometer respaldo por nube.
- El identificador Android anterior era distinto (`com.rentacuarto.expo`). El nuevo paquete no migra instalaciones Android automáticamente.
- El documento UML previo de otra rama incluye 23 RF; este repositorio usa **22 RF** del documento `RentaCuarto_Proyecto_UML_Profesional.docx` como única fuente actual.

**Antes de marcar como validado:** guardar comando, dispositivo/API, fecha, entrada de prueba, resultado esperado y observado, captura/log y responsable. Plantilla en [Pruebas](08_GUIA_DE_PRUEBAS.md).
