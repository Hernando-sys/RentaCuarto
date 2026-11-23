# 11 · Equipo, responsabilidades y colaboración

Basado en la sección 8 del documento UML profesional. Los responsables representan **distribución planificada**, no necesariamente autoría final de cada commit.

| Integrante | Responsabilidad base | RF asignados | Aporte transversal |
|---|---|---|---|
| JeanCarlos Huanacuni Gomez | Propiedades y habitaciones | RF01–RF03 | Configuración Android/Room, integración inicial |
| Álvaro Jesús Quispe Ñahui | Inquilinos, contratos y garantías | RF04–RF07, RF18 | Validación de vigencia y pruebas contractuales |
| Kevin Hernando Quispe Carbajal | Rentas, cargos y abonos | RF08–RF11, RF21 | Distribución de abonos e integridad económica |
| Marco Alejandro Tafur Ari | Panel y consultas | RF12–RF14, RF17, RF20 | Indicadores, historial y reportes |
| Rodrigo Contreras Peña | Gastos y evaluación | RF15–RF16, RF19, RF22 | Resultado económico y compartir información |

## Revisión cruzada planificada

- ER, Room y migraciones: **JeanCarlos**, revisión de **Kevin**.
- Contratos y garantías: **Álvaro**, revisión de **Kevin**.
- Rentas y abonos: **Kevin**, revisión de **Álvaro**.
- Panel e informes: **Marco**, revisión de **Rodrigo**.
- Gastos y resultados: **Rodrigo**, revisión de **Marco**.
- Demo e informe: **todo el grupo**, evidencia conjunta.

## Flujo de trabajo

1. Abrir issue con RF, alcance y criterio verificable.
2. Crear rama desde `main` y realizar cambios acotados.
3. Ejecutar Sync/Build/test y registrar resultado exacto.
4. Abrir PR y enlazar documentación; solicitar revisión cruzada.
5. Integrar tras revisión y verificación de navegación / DB cuando corresponda.

El cronograma académico se expresa en fases **T1–T10**; debe asignarse a fechas reales después de confirmarlas con el docente.
