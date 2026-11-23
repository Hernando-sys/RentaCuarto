# 01 · Descripción y alcance

## Contexto

RentaCuarto es una solución Android de uso local para la administración mensual de habitaciones residenciales por parte de un **Propietario/Administrador**. El inquilino es responsable de un contrato, pero no tiene sesión ni acceso propio a la aplicación.

Centraliza propiedades, habitaciones, inquilinos, contratos, rentas, cargos por servicios, abonos, depósitos en garantía y gastos. Las consultas económicas son derivadas; no debe almacenarse una copia redundante de cada saldo.

## Objetivos específicos (UML, sección 1)

- **OE01:** disponibilidad confiable de habitaciones y coherencia con contratos.
- **OE02:** obligaciones mensuales consistentes y sin duplicación.
- **OE03:** seguimiento de cobranza, abonos, vencimientos y deudas.
- **OE04:** historial y trazabilidad contractual y de garantías.
- **OE05:** informes mensuales con cobros efectivamente recibidos y gastos clasificados.

## Organización del producto

**A · Inmuebles:** RF01–03. **B · Inquilinos/contratos:** RF04–07 y RF18. **C · Cobros:** RF08–11, RF19 y RF21. **D · Panel/informes:** RF12–14, RF17, RF20 y RF22. **E · Gastos:** RF15–16.

## Restricciones actuales

- Android con **minSdk 24**; Kotlin, Jetpack Compose + Material 3, ViewModel, Room y SQLite.
- Aplicación **sin servidor, sin autenticación y sin conexión obligatoria**.
- Montos en **Long céntimos**, fechas en formato `AAAA-MM-DD` y periodos `AAAA-MM`.
- La **fecha fin pactada es informativa**; únicamente `fechaFinReal` finaliza el contrato.
- La garantía **pactada** no equivale a garantía **recibida**.
- El resultado económico es **operativo**, no utilidad contable ni renta gravable.

## Delimitación del avance

El objetivo documentado abarca RF01–RF22. La interfaz actual permite completar el ciclo desde registrar una propiedad hasta finalizar un contrato. La base de datos incluye el modelo económico, pero las pantallas de cobro y gasto se desarrollarán posteriormente.

**Fuente:** [Documento UML](referencias/RentaCuarto_Proyecto_UML_Profesional.docx), apartados 1–4.
