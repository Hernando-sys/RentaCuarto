# Historial de cambios

Los cambios describen entregables de código y documentación. No se usan como sustituto de pruebas en dispositivo.

## [Sin publicar] — Mantenimiento del repositorio

- Normalizada la identidad Android (`com.rentacuarto`) y el nombre visible del producto.
- Retiradas del código versionado las plantillas y clases obsoletas de demostración.
- Añadidas las pruebas de cálculos y Room que estaban preparadas pero no publicadas en `main`.
- Ajustados `.gitignore` y `.gitattributes` para evitar archivos locales y diferencias de salto de línea.
- Aún falta verificar la compilación Android, las pruebas y la migración Room en dispositivo.

## [0.4.0] — Integración de datos y documentación

### Agregado
- Modelo Room v2 con **10 entidades y 10 DAO** según el UML profesional.
- Migración incremental v1→v2 y retrocarga de garantías **pactadas**, sin asumir recepción.
- Repositorios de cobros, garantías y gastos; utilitario puro de distribución y estados.
- Pruebas de lógica de cálculo y documentación profesional con trazabilidad **RF01–RF22**.
- 18 diagramas extraídos del documento de referencia, archivo UML y guía de integración.

### Ajustado
- Nombre visible de la app a **RentaCuarto** y textos temporales retirados.
- Configuración de desarrollo con datos ficticios aislados de Release.
- Normalización del identificador de aplicación Android.

### Verificación pendiente
- Compilación Android y validación real de Room v2/migración en emulador.
- Integración visual de los módulos económicos y pruebas de extremo a extremo.

## [0.3.x] — Avance funcional anterior

- Pantallas de Inicio, Propiedades, Habitaciones, Inquilinos y Contratos.
- CRUD básico, disponibilidad, registro y finalización contractual con base Room v1.
- Datos ficticios para desarrollo en compilaciones Debug.

> Las fechas y versiones anteriores no se infieren como Releases oficiales de GitHub. Los commits del repositorio son la fuente de cambios publicados.
