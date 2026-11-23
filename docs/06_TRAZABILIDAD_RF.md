# 06 · Requerimientos funcionales y trazabilidad

**Referencia:** matriz RF01–RF22 (páginas 4–5) del documento UML adjunto. Los nombres y prioridades se ajustan a ese documento; **no** a la antigua matriz de 23 RF.

**Estados:** `Parcial` = parte visible o de reglas; `Capa programada` = código sin pantalla ni prueba integral; `Pendiente` = falta funcionalidad principal. No se marca ningún RF como aprobado sin evidencia Android.

| RF | Proceso | Archivo/pantalla | Estado | Pendiente o alcance actual |
|---|---|---|---|---|
| RF01 | Gestionar propiedades | P03/P04 y edición | Parcial | CRUD; falta desactivar sin perder historial |
| RF02 | Gestionar habitaciones | P05/P06 y edición | Parcial | CRUD; protege contratos existentes |
| RF03 | Consultar disponibilidad puntual | P05 + ContratoDao | Parcial | Consulta por fecha en DAO; UI usa fecha actual |
| RF04 | Gestionar inquilinos | P07/P08 y edición | Parcial | Buscar nombre/DNI; falta búsqueda por teléfono |
| RF05 | Registrar contrato | P10 + ContratosRepository | Parcial | Alta transaccional y pacto garantía; mejorar solapes futuros |
| RF06 | Finalizar contrato | P12 + ContratosRepository | Parcial | Fin real e historial; falta advertencia de deuda/garantía |
| RF07 | Consultar historial contractual | ContratoDao | Parcial | Consultas por habitación/inquilino sin pantalla dedicada |
| RF08 | Generar rentas mensuales | CobrosRepository + RentaDao | Capa programada | Idempotencia/periodo; falta UI y QA instrumentada |
| RF09 | Gestionar abonos | CobrosRepository + AbonoDao | Capa programada | Registro, corrección/anulación; falta UI y QA |
| RF10 | Consultar renta individual | DAOs + CalculosCobros | Capa programada | Monto y estado derivables; falta detalle UI |
| RF11 | Consultar estado de cuenta | ConsultasRepository.estadoCuenta | Capa programada | Consulta cronológica básica; falta UI y QA histórica |
| RF12 | Consultar ocupación consolidada | P01/P05 | Parcial | Ocupación hoy por propiedad; falta fecha elegida |
| RF13 | Consultar deuda vencida | ConsultasRepository.deudasVencidas | Capa programada | Consulta a fecha (sobre movimientos actuales); falta UI y QA histórica |
| RF14 | Consultar cobros del mes | AbonoDao + ConsultasRepository | Capa programada | Suma y listado efectivos por propiedad/mes; falta UI y QA |
| RF15 | Gestionar gastos | GastosRepository + GastoDao | Capa programada | CRUD lógico de datos; falta interfaz y QA |
| RF16 | Resultado económico mensual | GastosRepository | Capa programada | Suma efectiva menos gastos; falta interfaz y QA |
| RF17 | Consultar alertas | P02 | Parcial | Próximos contratos a 30 días; umbrales configurables pendientes |
| RF18 | Gestionar garantía | GarantiaDao + CobrosRepository | Parcial | Pacto, recepción/liquidación en datos; falta UI y auditoría |
| RF19 | Compartir estado de cuenta | Sin pantalla | Pendiente | Falta estado de cuenta/constancia ACTION_SEND |
| RF20 | Consultar evolución histórica | Sin pantalla | Pendiente | Faltan consultas y filtros de tendencias por fecha |
| RF21 | Gestionar cargos por servicio | CobrosRepository + CargoServicioDao | Capa programada | Alta/edición/anulación + redistribución; falta UI y QA |
| RF22 | Gráficos comparativos | Sin pantalla | Pendiente | Faltan informes visuales entre periodos |

## Criterios de aceptación CA01–CA10

| Criterio | Caso a validar |
|---|---|
| CA01 | No permitir contrato superpuesto en habitación ocupada |
| CA02 | Finalizar con fecha real y liberar habitación preservando historia |
| CA03 | Generar dos veces periodo sin duplicarlo |
| CA04 | Abono parcial cubre primero la renta antigua; corrección redistribuye |
| CA05 | Conciliación del estado de cuenta con movimientos |
| CA06 | Gasto válido registrado; importe inválido rechazado |
| CA07 | Resultado mensual excluye origen GARANTIA |
| CA08 | Liquidación de garantía no supera lo recibido |
| CA09 | Modificar cargo recalcula exigible y aplicación sin dañar historia |
| CA10 | Persistencia y operación offline al cerrar/reabrir |

## Requerimientos no funcionales (RNF01–RNF16)

El documento base define compatibilidad mínima API 24, dispositivo de referencia 3 GB RAM/720×1280, panel ≤2 s en p95, persistencia, funcionamiento offline, interacción corta para abonos, controles de 48 dp, tipografía accesible, céntimos Long, fechas ISO, español, integridad, transacciones, pruebas en dos configuraciones, arquitectura Android/Room, PR revisados y UML/Figma/Jira. **Son metas de diseño**, no métricas ya medidas. Ver documento original, páginas 7–8.
