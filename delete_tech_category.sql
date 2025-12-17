-- Eliminar la categoría 'Tecnología' si existe
-- Primero movemos cualquier evento que pudiera estar en esa categoría a 'Otros/Conferencias' (ID 4)
-- Asumimos que existen las tablas y la categoría destino

SET SQL_SAFE_UPDATES = 0;

UPDATE eventos 
SET categoria_id = 4 
WHERE categoria_id IN (SELECT id FROM categorias WHERE nombre = 'Tecnología');

DELETE FROM categorias WHERE nombre = 'Tecnología';

SET SQL_SAFE_UPDATES = 1;
