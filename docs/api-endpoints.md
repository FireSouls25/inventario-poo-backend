# API REST - Sistema de Inventario de Restaurante

**Base URL:** `http://localhost:8080/api` (desarrollo) | `https://inventario-api.onrender.com/api` (producción)

**Autenticación:** JWT Bearer Token en header `Authorization: Bearer <token>`
- Access token expira en 15 minutos
- Refresh token expira en 7 días

---

## 1. Autenticación (`/api/auth`)

### POST /api/auth/register
Registrar un nuevo usuario (solo para seed inicial, luego los crea el ADMIN).

**Request:**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@restaurante.com",
  "password": "123456",
  "rol": "MESERO"
}
```

**Response (201):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "juan@restaurante.com",
  "rol": "MESERO",
  "nombre": "Juan Pérez"
}
```

**Errores:** 400 (email ya registrado), 400 (validación)

### POST /api/auth/login
Iniciar sesión.

**Request:**
```json
{
  "email": "admin@restaurante.com",
  "password": "admin123"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
  "email": "admin@restaurante.com",
  "rol": "ADMIN",
  "nombre": "Administrador"
}
```

**Errores:** 401 (credenciales inválidas)

### POST /api/auth/refresh
Refrescar token expirado.

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Response (200):** Misma estructura que login/register.

**Errores:** 400 (refresh token inválido o expirado)

---

## 2. Usuarios (`/api/usuarios`)
**Rol requerido:** ADMIN

### GET /api/usuarios
Listar todos los usuarios.

**Response (200):**
```json
[
  {
    "id": 1,
    "nombre": "Administrador",
    "email": "admin@restaurante.com",
    "rol": "ADMIN",
    "activo": true
  }
]
```

### GET /api/usuarios/{id}
Obtener usuario por ID.

**Response (200):** Objeto UsuarioResponse individual.
**Error:** 404 (no encontrado)

### POST /api/usuarios
Crear usuario.

**Request:**
```json
{
  "nombre": "María López",
  "email": "maria@restaurante.com",
  "password": "123456",
  "rol": "CHEF"
}
```

**Response (201):** UsuarioResponse creado.

### PUT /api/usuarios/{id}
Actualizar usuario.

**Request:** Mismo schema que POST.

**Response (200):** UsuarioResponse actualizado.

### PATCH /api/usuarios/{id}/desactivar
Desactivar usuario (soft delete).

**Response (200):**
```json
{ "mensaje": "Usuario desactivado correctamente" }
```

---

## 3. Productos (`/api/productos`)
**Roles:** GET públicos para autenticados, POST/PUT/PATCH requieren ADMIN.

### GET /api/productos
Listar productos activos.

**Response (200):**
```json
[
  {
    "id": 1,
    "nombre": "Tomate",
    "unidad": "GRAMO",
    "stockActual": 5000.000,
    "stockMinimo": 1000.000,
    "precioCompra": 2.50,
    "activo": true
  }
]
```

### GET /api/productos/{id}
Obtener producto por ID.

### GET /api/productos/stock-bajo
Listar productos con stockActual <= stockMinimo.
**Rol:** ADMIN

### POST /api/productos
Crear producto.
**Rol:** ADMIN

**Request:**
```json
{
  "nombre": "Tomate",
  "unidad": "GRAMO",
  "precioCompra": 2.50,
  "stockMinimo": 1000
}
```

### PUT /api/productos/{id}
Actualizar producto.
**Rol:** ADMIN

### PATCH /api/productos/{id}/desactivar
Desactivar producto (soft delete).
**Rol:** ADMIN

---

## 4. Platos (`/api/platos`)

### GET /api/platos
Listar todos los platos con sus ingredientes.

**Response (200):**
```json
[
  {
    "id": 1,
    "nombre": "Tortilla de Patatas",
    "precioVenta": 8.50,
    "disponible": true,
    "descripcion": "Tortilla española clásica",
    "ingredientes": [
      {
        "id": 1,
        "productoNombre": "Huevo",
        "cantidad": 4.000,
        "unidad": "UNIDAD"
      }
    ]
  }
]
```

### GET /api/platos/disponibles
Listar solo platos con disponible=true.

### GET /api/platos/{id}
Obtener plato por ID con ingredientes.

### POST /api/platos
Crear plato.
**Rol:** ADMIN

**Request:**
```json
{
  "nombre": "Tortilla de Patatas",
  "precioVenta": 8.50,
  "descripcion": "Tortilla española clásica"
}
```

### PUT /api/platos/{id}
Actualizar plato.
**Rol:** ADMIN

### PATCH /api/platos/{id}/disponibilidad
Alternar disponibilidad (true ↔ false).
**Rol:** ADMIN

---

## 5. Recetas (`/api/platos/{platoId}/receta`)

### GET /api/platos/{platoId}/receta
Listar ingredientes de un plato.

**Response (200):**
```json
[
  {
    "id": 1,
    "productoNombre": "Huevo",
    "cantidad": 4.000,
    "unidad": "UNIDAD"
  }
]
```

### POST /api/platos/{platoId}/receta
Agregar ingrediente a un plato.
**Rol:** ADMIN

**Request:**
```json
{
  "productoId": 1,
  "cantidad": 4
}
```

### PUT /api/platos/{platoId}/receta/{itemId}
Actualizar cantidad de un ingrediente.
**Rol:** ADMIN

**Request:**
```json
{
  "productoId": 1,
  "cantidad": 3
}
```

### DELETE /api/platos/{platoId}/receta/{itemId}
Eliminar ingrediente de la receta.
**Rol:** ADMIN

---

## 6. Movimientos de Stock (`/api/movimientos`)

### GET /api/movimientos
Listar movimientos con filtros opcionales.

**Query params:** productoId, tipo (ENTRADA/SALIDA/AJUSTE), fechaDesde, fechaHasta

**Response (200):**
```json
[
  {
    "id": 1,
    "tipo": "ENTRADA",
    "cantidad": 5000.000,
    "fecha": "2026-05-31T10:30:00",
    "proveedor": "Distribuidora ABC",
    "motivo": null,
    "productoId": 1,
    "productNombre": "Tomate",
    "usuarioEmail": "admin@restaurante.com",
    "platoId": null,
    "platoNombre": null
  }
]
```

### POST /api/movimientos/entrada
Registrar entrada de stock.
**Rol:** ADMIN

**Request:**
```json
{
  "productoId": 1,
  "cantidad": 5000,
  "proveedor": "Distribuidora ABC"
}
```

### POST /api/movimientos/salida
Registrar venta de un plato (descuenta ingredientes automáticamente).
**Rol:** MESERO, ADMIN

**Request:**
```json
{
  "platoId": 1
}
```

**Errores:** 400 (stock insuficiente), 400 (plato sin receta), 400 (plato no disponible)

### POST /api/movimientos/ajuste
Realizar ajuste manual de stock.
**Rol:** ADMIN

**Request:**
```json
{
  "productoId": 1,
  "cantidad": -500,
  "motivo": "Merma por caducidad"
}
```

---

## Códigos de Error Comunes

| Código | Significado |
|--------|------------|
| 400 | Bad Request - validación fallida o regla de negocio |
| 401 | Unauthorized - token inválido o ausente |
| 403 | Forbidden - rol sin permisos |
| 404 | Not Found - recurso no existe |
| 500 | Internal Server Error |

**Errores de validación (400):**
```json
{
  "nombre": "El nombre es obligatorio",
  "precioCompra": "El precio debe ser positivo"
}
```

**Errores simples (400, 401, 403, 404, 500):**
```json
{
  "mensaje": "Producto no encontrado con id: 99"
}
```

---

## Seed Data

Al iniciar la aplicación por primera vez con BD vacía, se crea automáticamente:

| Email | Contraseña | Rol |
|-------|-----------|-----|
| admin@restaurante.com | admin123 | ADMIN |
