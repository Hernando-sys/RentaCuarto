# 05 · Reglas de negocio (RN01–RN18)

Transcripción operativa y vínculo con código, conforme a la sección 2.3 del documento UML. **Programada** no equivale a validada en emulador.

| Regla | Qué exige el negocio | Implementación conocida |
|---|---|---|
| RN01 | Ocupación calculada según contrato en fecha | `ContratoDao.habitacionesOcupadas`; UI consulta hoy |
| RN02 | Sin contratos simultáneos por habitación | `ContratosRepository.registrar`; validación conservadora y transaccional |
| RN03 | Un tercero puede abonar sin cambiar al inquilino | Campo `AbonoEntity.pagador` opcional |
| RN04 | La fecha pactada no finaliza automáticamente | `ContratoEntity.fechaFinReal`, `ContratosRepository.finalizar` |
| RN05 | Renta única por `(contrato, periodo)` | Índice UNIQUE en `RentaEntity`; `CobrosRepository.generarRenta` |
| RN06 | No generar rentas posteriores a fin real | Validación en `generarRenta` |
| RN07 | Precio de contrato no altera rentas anteriores | Base copiada a `RentaEntity` al generar; UI de renovación pendiente |
| RN08 | Exigible = renta base + cargos vigentes | `CobrosRepository.redistribuirDentroDeTransaccion` |
| RN09 | Aplicar abonos a rentas antiguas | `CalculosCobros.distribuir`, DAO ordenado |
| RN10 | Saldo = exigible – abonos; negativo es a favor | `CalculosCobros.saldoContrato` |
| RN11 | PAGADA, VENCIDA o PENDIENTE según saldo y fecha | `CalculosCobros.estadoRenta` |
| RN12 | Adelantadas = condición contractual informativa | `ContratoEntity.numeroRentasAdelantadas` |
| RN13 | Gastos según catálogo de seis categorías | `GastosRepository.guardar` |
| RN14 | Pactado ≠ recibido | `GarantiaEntity`; inicial `recibidoCent=0` |
| RN15 | Garantía recibida separada de rentas; origen GARANTIA | `registrarRecepcionGarantia`, `liquidarGarantia` |
| RN16 | Resultado = abonos efectivos – gastos del mes | `GastosRepository.resultadoMensual`, DAO filtra `origen='EFECTIVO'` |
| RN17 | Abonos positivos; correcciones recomponen reparto y dejan rastro | `registrarAbono`, `corregirAbono`, `anularAbono` |
| RN18 | Operaciones multi-tabla atómicas | `database.withTransaction` en repositorios |

## Ejemplo económico reproducible

Una renta base de S/ 500 y un cargo de S/ 30 crean **S/ 530 exigibles**. Dos abonos de S/ 200 y S/ 330 deben aplicarse a esa renta. Su saldo queda en S/ 0 y su estado es **PAGADA**. La distribución no debe superar el importe del abono ni el exigible de la renta.

## Garantía y resultado

Una garantía de S/ 500 pactados con S/ 300 recibidos no permite devolver S/ 350. Una aplicación de S/ 100 a renta debe registrarse como `origen=GARANTIA` y **no contar como nuevo ingreso**.

Si los cobros efectivos fueron S/ 1 000 y los gastos S/ 270, el resultado operativo es **S/ 730**, aunque existan S/ 100 de imputación interna de garantía.

## Limitaciones y validaciones faltantes

La regla conservadora actual bloquea registrar un nuevo contrato si hay otro sin fin real, incluso si comienza en fecha futura. No se declara todavía que el algoritmo admita todas las agendas futuras sin solapamiento. Faltan también alertas de deuda/garantía al finalizar, historial detallado en UI, auditoría de todas las recepciones y una batería de pruebas E2E completa.
