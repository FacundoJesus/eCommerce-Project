# E-Commerce Full Stack

## Stack
### Backend (ecom-backend)
- Java 25
- Spring Boot (4.0.4)
- Spring Data JPA
- Spring Security & JWT
- PostgreSQL
- Maven
- Lombok
- Stripe Java
- Swagger (Springdoc)

### Frontend (ecom-frontend)
- React 19
- Vite
- Redux Toolkit
- Tailwind CSS
- Material UI (MUI)
- Axios
- React Hook Form
- Stripe React

## Comandos
### Backend
- Ejecutar en desarrollo: `mvn spring-boot:run` (desde la carpeta `ecom-backend`)
- Compilar: `mvn clean install`

### Frontend
- Instalar dependencias: `npm install`
- Ejecutar en desarrollo: `npm run dev` (desde la carpeta `ecom-frontend`)
- Compilar para producción: `npm run build`

## Estructura del proyecto
- `/ecom-backend`: Código fuente del backend (API REST en Spring Boot).
- `/ecom-frontend`: Código fuente del frontend (SPA en React).

## Convenciones
- **Backend**: Arquitectura en capas (Controller, Service, Repository, Model). Uso de DTOs para transferencia de datos.
- **Frontend**: Uso de componentes funcionales y Hooks. Gestión de estado global con Redux Toolkit.
- Mantenemos la separación estricta entre presentación y lógica de negocio.
- Uso de variables de entorno para la configuración sensible.

## No hagas
- No exponer claves secretas o credenciales en el código fuente (ej. JWT secret, credenciales de base de datos, Stripe keys).
- No realizar lógica de negocio compleja en los controladores del backend o en las vistas del frontend.
- No modificar el código generado automáticamente sin revisar la configuración de Lombok/JPA.

## Flujo de trabajo
- Desarrollo y prueba del backend, validando el funcionamiento mediante la interfaz de Swagger.
- Integración del frontend usando Axios para consumir los endpoints REST de manera desacoplada.

## Documentación
- La API del backend se documenta mediante Swagger (Springdoc OpenAPI).
