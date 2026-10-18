# RentaCuarto

Aplicación móvil con la que un propietario registra sus habitaciones en alquiler, sus inquilinos, lo que les cobra y lo que gasta, para saber en todo momento quién debe, cuánto y cuánto deja cada propiedad.

## Lenguaje

### Personas

**Propietario**:
La persona que administra las propiedades y el único usuario de la aplicación, sea o no el dueño legal.
_Evitar_: dueño, administrador, arrendador, usuario

**Inquilino**:
La persona responsable de un contrato. No usa la aplicación.
_Evitar_: arrendatario, cliente, huésped

### Inmuebles

**Propiedad**:
Una casa o edificio del propietario con habitaciones que alquila como vivienda por meses.
_Evitar_: inmueble, local, terreno

**Habitación**:
La unidad que se alquila dentro de una propiedad; está disponible u ocupada.
_Evitar_: cuarto, unidad

### Contrato

**Contrato**:
El acuerdo por el que un inquilino ocupa una habitación pagando un monto de renta mensual desde una fecha de inicio; sigue vigente hasta su fecha de fin.
_Evitar_: alquiler, arriendo

**Fecha de fin**:
El día en que el contrato termina realmente; mientras no se registra, el contrato sigue vigente.

**Fecha de fin pactada**:
El plazo acordado al firmar; es informativa y no termina el contrato.
_Evitar_: vencimiento del contrato

**Depósito en garantía**:
Dinero que el inquilino entrega al iniciar el contrato, igual a un número de rentas (las rentas en garantía); no forma parte del saldo.
_Evitar_: garantía (a secas), depósito

### Cobros

**Cobro**:
Todo monto que el inquilino debe pagar: una renta o un cargo por servicio.
_Evitar_: deuda, cargo (a secas)

**Renta**:
El cobro mensual de un contrato por el monto de la renta completo, que vence cada mes el mismo día en que empezó el contrato; no hay primer mes prorrateado. Es la merced conductiva.
_Evitar_: cuota, mensualidad, alquiler

**Cargo por servicio**:
Un cobro que el propietario registra por el consumo de un servicio de la habitación (luz, agua, gas, internet u otro), con el monto que él determina.
_Evitar_: gasto de servicio, recargo

**Estado de cobro**:
Pagado, pendiente o vencido; resulta de aplicar los pagos a los cobros de vencimiento más antiguo primero. Un cobro no cubierto está pendiente el día de su vencimiento y vencido desde el día siguiente; no hay días de tolerancia.

**Pago**:
El abono que recibe el propietario, registrado contra un contrato y no contra un cobro en particular; puede hacerlo el inquilino o un tercero.
_Evitar_: depósito (se confunde con el depósito en garantía)

**Saldo**:
Total de cobros de un contrato menos total de pagos; si es positivo es deuda, si es negativo es saldo a favor del inquilino.
_Evitar_: deuda (como dato guardado), balance

**Estado de cuenta**:
La lista cronológica de cobros y pagos de un contrato con el saldo acumulado; es lo que se comparte con el inquilino.
_Evitar_: reporte, historial

**Línea del estado de cuenta**:
Un cobro dentro del estado de cuenta, con el monto cubierto por los pagos, el monto por cobrar y su estado de cobro.
_Evitar_: línea de cuota

### Gastos

**Gasto**:
Dinero que el propietario paga por una propiedad, clasificado en una categoría de gasto.
_Evitar_: egreso, costo

**Categoría de gasto**:
La clasificación fija de un gasto: servicios básicos, arbitrios e impuestos, mantenimiento y reparaciones, limpieza, equipamiento u otros.

**Gasto de servicio**:
Un gasto de la categoría servicios básicos: lo que el propietario paga a la empresa de luz, agua, gas o internet. No es un cargo por servicio, que es lo que el inquilino le debe al propietario.

**Resultado mensual**:
Los pagos recibidos en un mes por los contratos de una propiedad menos los gastos de esa propiedad en ese mes.
_Evitar_: ganancia, utilidad, ingreso neto
