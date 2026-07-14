# Sucursal (ms-Sucursal)

Gestion de sucursales de la libreria, transferencias entre sucursales, y solicitudes de reposicion.

## Puerto

**8086** | DB: `sucursal_ms`

## Endpoints

### Sucursales (`/api/sucursales`)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/sucursales` | Listar todas |
| GET | `/api/sucursales/{id}` | Obtener por ID |
| POST | `/api/sucursales` | Crear sucursal |
| PUT | `/api/sucursales/{id}` | Actualizar sucursal |
| DELETE | `/api/sucursales/{id}` | Eliminar sucursal |

### Transferencias (`/api/transferencias`)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/transferencias` | Listar todas |
| GET | `/api/transferencias/{id}` | Obtener por ID |
| POST | `/api/transferencias` | Crear transferencia |
| PUT | `/api/transferencias/{id}` | Actualizar transferencia |
| DELETE | `/api/transferencias/{id}` | Eliminar transferencia |
| GET | `/api/transferencias/sucursal/{idSucursal}` | Transferencias de una sucursal |

### Reposiciones (`/api/reposiciones`)

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| GET | `/api/reposiciones` | Listar todas |
| GET | `/api/reposiciones/{id}` | Obtener por ID |
| POST | `/api/reposiciones` | Crear solicitud |
| PUT | `/api/reposiciones/{id}` | Actualizar solicitud |
| DELETE | `/api/reposiciones/{id}` | Eliminar solicitud |
| GET | `/api/reposiciones/sucursal/{idSucursal}` | Solicitudes de una sucursal |

## Crear sucursal

```json
POST /api/sucursales
{
  "idAdminGeneral": 1,
  "idGerenteSede": 1,
  "nombre": "Sucursal Centro",
  "direccion": "Av. Principal 123, Santiago",
  "fechaInicio": "2025-01-15",
  "telefono": "+56212345678",
  "email": "centro@libreria.cl",
  "estado": true
}
```

## Ejecucion

```cmd
cd ms-Sucursal
.\mvnw.cmd spring-boot:run
```
