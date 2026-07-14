# Sucursal — Gestion de Locales

## Que es

Administra las tiendas fisicas de la libreria (sucursales), las transferencias de libros entre locales, y las solicitudes de reposicion a proveedores. Piensa en este servicio como el "gerente central" que coordina el movimiento de mercaderia entre las distintas sucursales.

## Las sucursales

Cada sucursal tiene nombre, direccion, telefono, email, y dos referencias a usuarios del sistema:
- **idAdminGeneral** — El administrador corporativo a cargo.
- **idGerenteSede** — El gerente que opera ese local en particular.

Actualmente el sistema tiene 2 sucursales: **Sucursal Centro** (Santiago) y **Sucursal Norte** (Antofagasta).

## Transferencias entre sucursales

El caso de uso principal: una sucursal tiene libros de mas que otra necesita. El proceso completo es:

```
Sucursal A                          Sucursal B
    |                                     |
    +---- crear transferencia ----------->+
              (items: libro + cantidad)
                        |
                   [Aprobar]
                        |
         +--------------+--------------+
         |              |              |
    reducir stock   sumar stock   crear envio
    en origen (A)   en destino (B)  para rastrear
```

**Cuando se aprueba una transferencia**, ocurren 3 cosas automaticamente:
1. Se descuenta el stock en la sucursal de origen (via Inventario :8094).
2. Se suma el stock en la sucursal de destino (via Inventario :8094).
3. Se crea un registro de envio (via Envios :8084) para rastrear el despacho fisico.

Esto es la unica parte del sistema donde un microservicio llama a otros de forma automatica en una transaccion.

## Solicitudes de reposicion

Cuando una sucursal se queda sin libros, puede pedir reposicion. El ciclo es: `Pendiente` -> `Aprobada`/`Rechazada` -> `Enviada` -> `Recibida`. Este flujo esta definido pero aun no tiene integracion con el servicio de proveedores.

## Ejecutar

```cmd
cd ms-Sucursal
.\mvnw.cmd spring-boot:run
```

Puerto: **8086** | DB: `sucursal_ms`

## Endpoints

**Sucursales** (`/api/sucursales`): CRUD completo.

**Transferencias** (`/api/transferencias`): Crear, listar, actualizar estado, ver por sucursal.
- `PUT /api/transferencias/{id}` con `estado: "Aprobada"` dispara la logica de stock + envio.

**Reposiciones** (`/api/reposiciones`): Crear, listar, actualizar estado, ver por sucursal.
