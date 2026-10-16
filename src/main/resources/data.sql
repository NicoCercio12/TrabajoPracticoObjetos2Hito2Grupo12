-- ==========================================================
-- SCRIPT DE CARGA DE DATOS: EPICENTRO GOURMET
-- ==========================================================

-- 1. Insertar Festivales (Caso de Uso 1)
INSERT INTO festival (id, nombre, temporada, fecha_inicio, fecha_fin, created_at, updated_at) VALUES 
(1, 'Festival Primavera Gourmet', 'Primavera', '2026-09-10', '2026-09-25', NOW(), NOW()),
(2, 'Burger & Beer Fest Lanús', 'Primavera', '2026-10-01', '2026-10-05', NOW(), NOW()),
(3, 'Feria Tradicional de Invierno', 'Invierno', '2026-07-01', '2026-07-15', NOW(), NOW());

-- 2. Insertar Empleados / Responsables
-- Asumiendo una tabla empleado o persona (herencia según su modelo)
-- Si tienen tabla persona/empleado cargamos los responsables:
INSERT INTO persona (id, nombre, apellido, email, created_at, updated_at) VALUES 
(1, 'Martín', 'Palermo', 'martin@epicentro.com', NOW(), NOW()),
(2, 'Román', 'Riquelme', 'roman@epicentro.com', NOW(), NOW())
ON DUPLICATE KEY UPDATE nombre=nombre;

-- 3. Insertar Unidades de Venta (Caso de Uso 2)
-- Si usan estrategia SINGLE_TABLE o JOINED para UnidadDeVenta:
-- En caso de JOINED:
INSERT INTO unidad_de_venta (id, codigo_unico, nombre_comercial, superficie, responsable_id) VALUES 
(1, 'FTK-PRIM-01', 'La Burger Rodante', 25.5, 1),
(2, 'PST-TACO-02', 'Tacos Chingones', 18.0, 2);

-- Insertar detalles de FoodTruck
INSERT INTO food_truck (id, patente, requiere_electricidad) VALUES 
(1, 'AF123JK', 1);

-- Insertar detalles de PuestoDesarmable
INSERT INTO puesto_desarmable (id, cant_carpas, tiempo_montaje_minutos) VALUES 
(2, 2, 45);

-- 4. Asociar Unidades de Venta al Festival 1 (Tabla intermedia ManyToMany)
INSERT INTO festival_unidad_de_venta (festival_id, unidad_de_venta_id) VALUES 
(1, 1),
(1, 2);