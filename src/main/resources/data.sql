-- 1. Insertar Roles de usuario
INSERT INTO user_role (id, role, created_at, updated_at) VALUES
                                                             (1, 'ROLE_RESPONSABLE', NOW(), NOW()),
                                                             (2, 'ROLE_EMPLEADO', NOW(), NOW());

-- 2. Insertar Usuario Responsable (Usuario: responsable1 / Pass: 1234)
INSERT INTO user (id, username, password, enabled, created_at, updated_at) VALUES
    (1, 'responsable1', '$2a$10$E2UPv7arXym3L0.p1bT9e.vU.X0H6vY3A7hU/S5oK0Wv4x8Q9L/Gu', true, NOW(), NOW());

INSERT INTO user_user_role (user_id, role_id) VALUES (1, 1);

-- 3. Insertar 2 Festivales
INSERT INTO festival (id, nombre, temporada, fecha_inicio, fecha_fin, created_at, updated_at) VALUES
                                                                                                  (1, 'Gourmet Spring Fest', 'Primavera', '2026-10-01', '2026-11-08', NOW(), NOW()),
                                                                                                  (2, 'Beer & Burger Fest', 'Verano', '2026-11-10', '2026-12-31', NOW(), NOW());

-- 4. Insertar Unidades de Venta
INSERT INTO unidad_de_venta (id, nombre_comercial, codigo_unico, superficie, created_at, updated_at) VALUES
                                                                                                         (1, 'Burger Zone Truck', 'FT-001', 12.5, NOW(), NOW()),
                                                                                                         (2, 'Puesto Parrilla Criolla', 'PD-002', 20.0, NOW(), NOW());

INSERT INTO food_truck (id, patente, requiere_electricidad) VALUES
    (1, 'AB123CD', true);

INSERT INTO puesto_desarmable (id, cant_carpas, tiempo_montaje_minutos) VALUES
    (2, 2, 30);

-- 5. Insertar Empleados
INSERT INTO empleado (id_empleado, nombre, apellido, dni, email, fecha_nacimiento, fecha_ingreso, activo, created_at, updated_at) VALUES
                                                                                                                                      (1, 'Juan', 'Pérez', '38123456', 'juan@gmail.com', '1995-05-10', '2022-01-15', true, NOW(), NOW()),
                                                                                                                                      (2, 'María', 'Gómez', '40987654', 'maria@gmail.com', '1998-08-20', '2023-03-01', true, NOW(), NOW());

INSERT INTO cocinero (id_empleado, rol_cocina) VALUES
    (1, 'LAVAPLATOS');

INSERT INTO cajero (id_empleado, turno) VALUES
    (2, 'MANIANA');