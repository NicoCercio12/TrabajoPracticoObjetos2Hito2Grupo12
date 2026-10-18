-- 1. Insertar 2 Festivales
INSERT INTO festival (id, nombre, temporada, fecha_inicio, fecha_fin, duracion_dias, activo)
VALUES 
(1, 'Gourmet Spring Fest', 'Primavera', '2026-10-01', '2026-10-15', 15, true),
(2, 'Beer & Burger Fest', 'Verano', '2026-11-10', '2026-11-20', 10, true);

-- 2. Insertar 2 Unidades de Venta vinculadas a los festivales
-- (Ajustá el nombre de la foreign key si en tu base se llama festival_id o id_festival)
INSERT INTO unidad_de_venta (id, nombre, festival_id)
VALUES 
(1, 'Puesto Parrilla Criolla', 1),
(2, 'Burger Zone Truck', 2);