SET SQL_SAFE_UPDATES = 0;

-- Script para limpiar eventos y datos relacionados
-- Se eliminan en orden inverso a las dependencias

DELETE FROM reservas;
DELETE FROM tipos_entrada;
DELETE FROM eventos;

SET SQL_SAFE_UPDATES = 1;

-- Opcional: Reiniciar contadores de auto-incremento (depende de la base de datos, ej: H2/MySQL)
-- ALTER TABLE eventos AUTO_INCREMENT = 1;
-- ALTER TABLE tipos_entrada AUTO_INCREMENT = 1;
-- ALTER TABLE reservas AUTO_INCREMENT = 1;
