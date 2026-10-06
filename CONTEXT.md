# RentaCuarto

Aplicación móvil con la que un propietario registra sus habitaciones en alquiler, sus inquilinos y el dinero que recibe, para saber en todo momento quién debe y cuánto.

## Lenguaje

### Personas

**Propietario**:
La persona que administra las propiedades y el único usuario de la aplicación, sea o no el dueño legal.
_Evitar_: dueño, administrador, arrendador, usuario

**Inquilino**:
La persona que alquila una habitación. No usa la aplicación.
_Evitar_: arrendatario, cliente, huésped

### Inmuebles

**Propiedad**:
Un inmueble del propietario que contiene una o más habitaciones.
_Evitar_: inmueble, casa, local

**Habitación**:
La unidad que se alquila dentro de una propiedad; está disponible u ocupada.
_Evitar_: cuarto, unidad

### Alquiler y dinero

**Contrato**:
El acuerdo por el que un inquilino ocupa una habitación pagando una renta mensual desde una fecha de inicio; sigue vigente hasta que el propietario registra su fecha de fin.
_Evitar_: alquiler, arriendo

**Cuota**:
El cargo de un mes de un contrato, por la renta completa, que vence cada mes el mismo día en que empezó el contrato; no hay primer mes prorrateado.
_Evitar_: mensualidad, factura, recibo, cobro

**Pago**:
Dinero recibido de un inquilino, registrado contra su contrato y no contra una cuota en particular.
_Evitar_: abono, depósito (se confunde con el depósito de garantía)

**Saldo**:
Total de cuotas de un contrato menos total de pagos; si es positivo es deuda, si es negativo es saldo a favor del inquilino.
_Evitar_: deuda (como dato guardado), balance

**Estado de la cuota**:
Pagada, pendiente o vencida; resulta de aplicar los pagos a las cuotas más antiguas primero. Una cuota vencida es una cuota no cubierta cuya fecha de vencimiento ya pasó.

**Gasto**:
Dinero que el propietario paga por una propiedad, como una reparación, la limpieza o un servicio.
_Evitar_: egreso, costo

**Resultado mensual**:
Los pagos recibidos en un mes por los contratos de una propiedad menos los gastos de esa propiedad en ese mes.
_Evitar_: ganancia, utilidad, ingreso neto

**Estado de cuenta**:
La lista cronológica de cuotas y pagos de un contrato con el saldo acumulado; es lo que se comparte con el inquilino.
_Evitar_: reporte, historial
