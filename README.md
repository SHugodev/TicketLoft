# TicketLoft 🎫

**TicketLoft** es una plataforma web desarrollada como Trabajo de Fin de Grado para el ciclo de **Desarrollo de Aplicaciones Multiplataforma (DAM)**. [cite_start]Su objetivo es simplificar la gestión de eventos y la venta de entradas, poniendo el foco en la usabilidad y la claridad[cite: 3, 8].

## 🚀 Tecnologías utilizadas
* [cite_start]**Backend:** Java 17 con Spring Boot 3[cite: 112].
* [cite_start]**Persistencia:** MySQL y Spring Data JPA[cite: 12].
* [cite_start]**Seguridad:** Spring Security (Autenticación y Roles de usuario/admin)[cite: 237].
* [cite_start]**Frontend:** HTML, CSS, JavaScript y motor de plantillas Thymeleaf[cite: 11].

## ✨ Funcionalidades principales
* [cite_start]**Gestión de Eventos:** Creación, edición y eliminación de eventos con control de aforos[cite: 46, 105].
* [cite_start]**Sistema de Reservas:** Compra de entradas digitales para usuarios registrados[cite: 47, 68].
* [cite_start]**Panel de Administración:** Supervisión de eventos y gestión de usuarios por parte de un administrador[cite: 20, 48].
* [cite_start]**Seguridad:** Rutas protegidas según el rol del usuario (USER/ADMIN)[cite: 234, 237].

## 🛠️ Instalación y ejecución
1. Clonar el repositorio: `git clone https://github.com/tu-usuario/ticketloft.git`
2. [cite_start]Configurar la base de datos MySQL local[cite: 113].
3. Ajustar las credenciales en `src/main/resources/application.properties`.
4. Ejecutar la aplicación con Maven: `./mvnw spring-boot:run`

---
**Autor:** Hugo Sánchez - Graduado de DAM