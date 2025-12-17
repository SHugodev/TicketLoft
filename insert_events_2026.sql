-- Script para insertar eventos de prueba para 2026
-- Asume que existen categorías con IDs: 1 (Música), 2 (Teatro), 3 (Deportes), 4 (Otros/Conferencias)
-- Asume que existe un usuario administrador con ID 1

SET @admin_id = 1;

-- ==========================================
-- CATEGORÍA 1: MÚSICA
-- ==========================================

-- 1.1 Coldplay
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Coldplay: Music of the Spheres World Tour', 'Disfruta de la magia de Coldplay en su gira mundial 2026. Un espectáculo lleno de luces, energía y sostenibilidad.', '2026-06-15 21:00:00', 'Estadio Olímpico', 'Barcelona', 50000, 'https://images.unsplash.com/photo-1470229722913-7ea049c42081?auto=format&fit=crop&q=80', NOW(), true, 1, @admin_id, 'APROBADO');
SET @ev1 = (SELECT id FROM eventos WHERE nombre = 'Coldplay: Music of the Spheres World Tour' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev1, 'Pista General', 'Acceso a la zona de pista de pie', 95.00, 20000, true),
(@ev1, 'Grada Premium', 'Asiento numerado en grada baja', 120.00, 25000, true),
(@ev1, 'VIP Experience', 'Acceso prioritario y merch exclusivo', 350.00, 5000, true);

-- 1.2 Taylor Swift
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Taylor Swift: The Eras Tour II', 'La gira más grande de la historia regresa a Europa. Un recorrido por todas las eras musicales de Taylor.', '2026-07-20 20:30:00', 'Santiago Bernabéu', 'Madrid', 65000, 'https://images.unsplash.com/photo-1540039155733-5bb30b53aa14?auto=format&fit=crop&q=80', NOW(), true, 1, @admin_id, 'APROBADO');
SET @ev2 = (SELECT id FROM eventos WHERE nombre = 'Taylor Swift: The Eras Tour II' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev2, 'Front Stage', 'Zona más cercana al escenario', 250.00, 5000, true),
(@ev2, 'Pista', 'Acceso general a pista', 150.00, 20000, true),
(@ev2, 'Grada Alta', 'Visibilidad panorámica', 80.00, 40000, true);

-- 1.3 Primavera Sound
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Primavera Sound 2026', 'El festival de referencia en música indie, pop y electrónica. Tres días de música ininterrumpida junto al mar.', '2026-06-04 16:00:00', 'Parc del Fòrum', 'Barcelona', 80000, 'https://images.unsplash.com/photo-1533174072545-e8d4aa97edf9?auto=format&fit=crop&q=80', NOW(), true, 1, @admin_id, 'APROBADO');
SET @ev3 = (SELECT id FROM eventos WHERE nombre = 'Primavera Sound 2026' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev3, 'Abono General', 'Acceso a los 3 días del festival', 280.00, 60000, true),
(@ev3, 'Abono VIP', 'Acceso a zonas VIP y fast track', 500.00, 10000, true),
(@ev3, 'Entrada de Día', 'Acceso para un solo día (Jueves)', 125.00, 10000, true);

-- ==========================================
-- CATEGORÍA 2: TEATRO
-- ==========================================

-- 2.1 El Rey León
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('El Rey León - El Musical', 'El espectáculo que conmueve al mundo. La mayor producción musical jamás presentada en España.', '2026-03-10 20:00:00', 'Teatro Lope de Vega', 'Madrid', 1500, 'https://images.unsplash.com/photo-1546820389-44d77e1f3b31?auto=format&fit=crop&q=80', NOW(), true, 2, @admin_id, 'APROBADO');
SET @ev4 = (SELECT id FROM eventos WHERE nombre = 'El Rey León - El Musical' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev4, 'Platea Gold', 'Mejores asientos de la sala', 110.00, 500, true),
(@ev4, 'Anfiteatro Preferente', 'Primera fila de anfiteatro', 90.00, 500, true),
(@ev4, 'Visibilidad Reducida', 'Asientos laterales económicos', 45.00, 500, true);

-- 2.2 Cirque du Soleil
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Cirque du Soleil: OVO', 'Una inmersión trepidante en el mundo de los insectos. Acrobacias de alto nivel y una puesta en escena inolvidable.', '2026-04-15 19:00:00', 'Palau Sant Jordi', 'Barcelona', 12000, 'https://images.unsplash.com/photo-1596701625983-584742468351?auto=format&fit=crop&q=80', NOW(), true, 2, @admin_id, 'APROBADO');
SET @ev5 = (SELECT id FROM eventos WHERE nombre = 'Cirque du Soleil: OVO' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev5, 'Pista Premium', 'Asientos a pie de escenario', 130.00, 2000, true),
(@ev5, 'Nivel 100', 'Grada inferior centrada', 95.00, 5000, true),
(@ev5, 'Nivel 200', 'Grada superior', 60.00, 5000, true);

-- 2.3 Mago Pop
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('El Mago Pop: Nada es Imposible', 'El ilusionista más taquillero del mundo regresa con un show que desafía la lógica. Prepárate para asombrarte.', '2026-02-20 21:00:00', 'Teatro Nuevo Apolo', 'Madrid', 1200, 'https://images.unsplash.com/photo-1626084478310-4632298642a1?auto=format&fit=crop&q=80', NOW(), true, 2, @admin_id, 'APROBADO');
SET @ev6 = (SELECT id FROM eventos WHERE nombre = 'El Mago Pop: Nada es Imposible' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev6, 'Butaca Oro', 'Filas 1-5', 85.00, 200, true),
(@ev6, 'Patio de Butacas', 'Visión perfecta', 65.00, 800, true),
(@ev6, 'Club', 'Primer piso', 45.00, 200, true);

-- ==========================================
-- CATEGORÍA 3: DEPORTES
-- ==========================================

-- 3.1 Champions League
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Final Champions League 2026', 'El partido más importante del año en el fútbol europeo. Vive la emoción, la pasión y la gloria en directo desde Budapest.', '2026-05-30 20:45:00', 'Puskás Aréna', 'Budapest', 67215, 'https://images.unsplash.com/photo-1522778119026-d647f0565c6d?auto=format&fit=crop&q=80', NOW(), true, 3, @admin_id, 'APROBADO');
SET @ev7 = (SELECT id FROM eventos WHERE nombre = 'Final Champions League 2026' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev7, 'Categoría 1', 'Ubicación central lateral', 800.00, 20000, true),
(@ev7, 'Categoría 2', 'Ubicación córners', 500.00, 20000, true),
(@ev7, 'Categoría 3', 'Detrás de portería', 300.00, 27215, true);

-- 3.2 Formula 1
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Formula 1 Gran Premio de España 2026', 'La máxima categoría del automovilismo llega a Montmeló. Adrenalina, velocidad y los mejores pilotos del mundo.', '2026-06-07 15:00:00', 'Circuit de Barcelona-Catalunya', 'Montmeló', 100000, 'https://images.unsplash.com/photo-1574768363717-de744b7b22d1?auto=format&fit=crop&q=80', NOW(), true, 3, @admin_id, 'APROBADO');
SET @ev8 = (SELECT id FROM eventos WHERE nombre = 'Formula 1 Gran Premio de España 2026' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev8, 'Pelouse', 'Acceso a zonas de césped', 150.00, 40000, true),
(@ev8, 'Tribuna Principal', 'Frente a boxes y recta de meta', 450.00, 10000, true),
(@ev8, 'Tribuna Norte', 'La zona más técnica del circuito', 300.00, 50000, true);

-- 3.3 NBA Paris Game
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('NBA Paris Game 2026', 'El mejor baloncesto del mundo regresa a Europa. Los Lakers se enfrentan a los Celtics en un clásico histórico.', '2026-01-22 20:00:00', 'Accor Arena', 'París', 16000, 'https://images.unsplash.com/photo-1546519638-68e109498ad0?auto=format&fit=crop&q=80', NOW(), true, 3, @admin_id, 'APROBADO');
SET @ev9 = (SELECT id FROM eventos WHERE nombre = 'NBA Paris Game 2026' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev9, 'Courtside', 'A pie de pista, experiencia inmersiva', 2500.00, 200, true),
(@ev9, 'Lower Bowl', 'Grada baja excelente visión', 450.00, 8000, true),
(@ev9, 'Upper Bowl', 'Grada alta', 150.00, 7800, true);

-- ==========================================
-- CATEGORÍA 4: OTROS (CONFERENCIAS/EVENTOS)
-- ==========================================

-- 4.1 Spring I/O
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Spring I/O 2026', 'La conferencia líder en Europa sobre el ecosistema Spring. Charlas y networking con expertos Java.', '2026-05-21 09:00:00', 'Fira de Barcelona', 'Barcelona', 2000, 'https://images.unsplash.com/photo-1556761175-5973dc0f32e7?auto=format&fit=crop&q=80', NOW(), true, 4, @admin_id, 'APROBADO');
SET @ev10 = (SELECT id FROM eventos WHERE nombre = 'Spring I/O 2026' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev10, 'General', 'Acceso a conferencias', 450.00, 1500, true),
(@ev10, 'Full Experience', 'Incluye workshop pre-conferencia', 750.00, 500, true);

-- 4.2 MWC
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Mobile World Congress 2026', 'El evento de conectividad más grande e influyente del mundo. Descubre el futuro de la tecnología móvil.', '2026-03-02 09:00:00', 'Fira Gran Via', 'Barcelona', 100000, 'https://images.unsplash.com/photo-1519389950473-47ba0277781c?auto=format&fit=crop&q=80', NOW(), true, 4, @admin_id, 'APROBADO');
SET @ev11 = (SELECT id FROM eventos WHERE nombre = 'Mobile World Congress 2026' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev11, 'Discovery Pass', 'Acceso a la exhibición', 800.00, 80000, true),
(@ev11, 'Leader Pass', 'Acceso a conferencias keynote', 2500.00, 15000, true),
(@ev11, 'VIP Networking', 'Acceso exclusivo y cenas', 4500.00, 5000, true);

-- 4.3 Japan Weekend
INSERT INTO eventos (nombre, descripcion, fecha, lugar, ciudad, aforo, imagen_url, fecha_creacion, activo, categoria_id, creado_por, estado_aprobacion)
VALUES ('Japan Weekend Madrid 2026', 'El evento de cultura pop japonesa más grande de España. Cosplay, anime, manga y videojuegos.', '2026-02-14 10:00:00', 'IFEMA', 'Madrid', 40000, 'https://images.unsplash.com/photo-1560964645-c174246c4344?auto=format&fit=crop&q=80', NOW(), true, 4, @admin_id, 'APROBADO');
SET @ev12 = (SELECT id FROM eventos WHERE nombre = 'Japan Weekend Madrid 2026' LIMIT 1);
INSERT INTO tipos_entrada (evento_id, nombre, descripcion, precio, cantidad_disponible, activo) VALUES
(@ev12, 'Sábado', 'Entrada válida para el sábado', 20.00, 20000, true),
(@ev12, 'Domingo', 'Entrada válida para el domingo', 18.00, 15000, true),
(@ev12, 'Abono 2 días', 'Acceso todo el fin de semana', 35.00, 5000, true);
