# Contexto de dominio de RentaCuarto

Este archivo resume los conceptos del negocio para mantener consistentes los nombres en el código y la documentación. La referencia principal es `docs/referencias/RentaCuarto_Proyecto_UML_Profesional.docx`, sección «Diccionario de términos».

- **Propietario / Administrador:** único usuario que opera la aplicación; gestiona propiedades, contratos, rentas y gastos.
- **Propiedad residencial:** casa o pequeño edificio que contiene habitaciones alquiladas mensualmente.
- **Habitación:** unidad física alquilable; su ocupación **se calcula** desde contratos, no es una columna fija.
- **Inquilino:** responsable contractual. No inicia sesión en la aplicación.
- **Contrato:** vincula habitación e inquilino con renta mensual, fecha inicio, primer vencimiento, fin pactado y fin real.
- **Fecha fin pactada:** previsión informativa; no finaliza automáticamente el contrato.
- **Fecha fin real:** fecha efectiva de cierre; puede liberar habitación y conserva el historial.
- **Renta mensual:** obligación correspondiente a un periodo único `AAAA-MM` por contrato, con renta base congelada.
- **Cargo por servicio:** valor adicional vinculado a una renta (agua, luz, gas, internet u otro).
- **Abono:** pago parcial o total registrado para un contrato. Puede realizarlo un tercero.
- **Aplicación de abono:** parte de un abono asignada a una renta; se distribuye primero a las más antiguas.
- **Depósito en garantía:** importe pactado que se registra **separado** del recibido; se devuelve o aplica a deuda al cierre.
- **Origen del abono:** `EFECTIVO` (dinero recibido para rentas) o `GARANTIA` (aplicación interna, no ingreso efectivo adicional).
- **Saldo:** exigible menos abonos imputables; saldo negativo indica crédito a favor del inquilino.
- **Estado de renta:** `PAGADA`, `PENDIENTE` o `VENCIDA`, derivado de exigible, pagos aplicados y fecha.
- **Gasto de propiedad:** desembolso de una propiedad, clasificado por categoría y fecha.
- **Resultado económico mensual:** abonos **efectivos** de un periodo menos gastos de la propiedad en ese periodo. No equivale a utilidad contable.

## Arquitectura y límites

Kotlin + Jetpack Compose/Material 3 → ViewModel → Repository → DAO/Room → SQLite local. La aplicación no tiene aún cuentas, servidor, sincronización ni respaldo en nube. El modelo previsto es de **diez tablas** y **22 requisitos funcionales**.

## Enlaces de uso interno

- [`docs/06_TRAZABILIDAD_RF.md`](docs/06_TRAZABILIDAD_RF.md): alcance y cobertura.
- [`docs/05_REGLAS_DE_NEGOCIO.md`](docs/05_REGLAS_DE_NEGOCIO.md): RN01–RN18.
- [`docs/07_ESTADO_Y_PENDIENTES.md`](docs/07_ESTADO_Y_PENDIENTES.md): avance real, sin confundir el UML futuro con la interfaz actual.
