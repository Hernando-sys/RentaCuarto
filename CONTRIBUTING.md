# Cómo contribuir a RentaCuarto

Agradecemos cambios que sean **comprobables, revisables y consistentes con el documento UML vigente**.

## Antes de cambiar código

- Lee [`README.md`](README.md), [`docs/INDICE.md`](docs/INDICE.md) y la trazabilidad del RF correspondiente.
- No reescribas el diseño sin coordinarlo con el grupo; el documento UML profesional contiene **22 RF**.
- Confirma primero que la rama `main` esté actualizada y crea una rama propia.

```bash
git switch main
git pull origin main
git switch -c feat/nombre-corto
```

## Pull Request mínimo

- Explica **qué cambia**, **por qué** y qué RF afecta.
- Incluye **prueba ejecutada** (comando y resultado), emulador/API y capturas si cambia la UI.
- Si cambia Room, incrementa versión, crea migración y demuestra conservación de datos.
- Actualiza `CHANGELOG.md`, `docs/07_ESTADO_Y_PENDIENTES.md` y pruebas relacionadas cuando corresponda.
- Solicita revisión de otro integrante; evita fusionar cambios críticos sin comprobación.

## Reglas de integridad

- No eliminar contratos ni movimientos históricos para corregir un saldo.
- Conservar `origen=GARANTIA` separado del medio de pago y de los cobros efectivos.
- No calcular pagos con `Float` ni `Double`.
- No subir carpetas `.idea`, `.gradle`, bases `.db`, contraseñas, tokens ni datos personales reales.
- Los **datos ficticios** viven solamente en `app/src/debug/`.

## Convención de commits

Ejemplos: `feat: agregar estado de cuenta`, `fix: impedir renta duplicada`, `test: cubrir reparto de abonos`, `docs: actualizar matriz de RF`.
