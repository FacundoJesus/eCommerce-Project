<h1 align="center">🛒 E-Commerce Full Stack Platform</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB" alt="React" />
  <img src="https://img.shields.io/badge/Redux-593D88?style=for-the-badge&logo=redux&logoColor=white" alt="Redux" />
  <img src="https://img.shields.io/badge/Tailwind_CSS-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white" alt="Tailwind CSS" />
  <img src="https://img.shields.io/badge/MUI-007FFF?style=for-the-badge&logo=mui&logoColor=white" alt="Material UI" />
  <img src="https://img.shields.io/badge/Stripe-626CD9?style=for-the-badge&logo=Stripe&logoColor=white" alt="Stripe" />
</p>

<p align="center">
  Una aplicación de comercio electrónico robusta y escalable desarrollada con <strong>Java 25</strong>, <strong>Spring Boot 4</strong> y <strong>React 19</strong>.
  <br />
  <br />
  <a href="https://e-commerce-project-three-amber.vercel.app/"><strong>🌍 Ver Despliegue en Vivo »</strong></a>
  ·
  <a href="#-funcionalidades-principales">Explorar Funcionalidades</a>
  ·
  <a href="#-cómo-ejecutar-el-proyecto-localmente">Instalación Local</a>
</p>

---

## 📖 Sobre el Proyecto

Este proyecto es una aplicación **full stack de nivel productivo** desarrollada para el portfolio personal. El objetivo principal fue construir una arquitectura escalable aplicando buenas prácticas de desarrollo, separación estricta de responsabilidades y las tecnologías más modernas del ecosistema Java y JavaScript.

Incluye el ciclo completo de un eCommerce: desde la gestión de productos y categorías, hasta la autenticación segura de usuarios, gestión del carrito, órdenes de compra e integración de pagos.

## ✨ Funcionalidades Principales

*   **🔐 Seguridad y Autenticación:** Registro e inicio de sesión seguro con **Spring Security** y **JWT**. Gestión de roles (Administrador / Usuario Cliente).
*   **🛍️ Catálogo Dinámico:** Exploración de productos con búsqueda avanzada, paginación y filtros por categorías.
*   **🛒 Carrito y Checkout:** Gestión ágil del carrito de compras, direcciones de envío y un proceso de checkout fluido.
*   **💳 Integración de Pagos:** Implementación de flujos de pago con **Stripe** (Frontend y Backend).
*   **📦 Panel de Administración:** Herramientas exclusivas para que los administradores gestionen productos, categorías y el estado de los pedidos.
*   **📱 Diseño Moderno y Responsivo:** Interfaz de usuario intuitiva y adaptable a móviles construida con **Tailwind CSS** y **Material UI (MUI)**.
*   **📄 Documentación Interactiva:** API REST completamente documentada y explorable gracias a **Swagger (Springdoc OpenAPI)**.

## 🛠️ Tecnologías y Herramientas

### ⚙️ Backend (`/ecom-backend`)
*   **Core:** Java 25, Spring Boot 4.0.4
*   **Persistencia de Datos:** Spring Data JPA, Hibernate, PostgreSQL
*   **Seguridad:** Spring Security, JWT (JSON Web Tokens)
*   **Herramientas:** Maven, Lombok, DTOs Pattern
*   **Integraciones:** Stripe Java, Swagger (Springdoc)

### 🎨 Frontend (`/ecom-frontend`)
*   **Core:** React 19, Vite
*   **Gestión de Estado y Enrutamiento:** Redux Toolkit, React Router
*   **Estilos y Componentes:** Tailwind CSS, Material UI (MUI)
*   **Formularios y Peticiones:** React Hook Form, Axios
*   **Integraciones:** Stripe React

## 🚀 Cómo Ejecutar el Proyecto Localmente

Para probar este proyecto en tu máquina local, asegúrate de tener instalado **Java 25**, **Node.js**, **Maven** y **PostgreSQL**.

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/eCommerce-Project.git
cd eCommerce-Project
```

### 2. Configurar y levantar el Backend
1.  Navega al directorio del backend:
    ```bash
    cd ecom-backend
    ```
2.  Crea una base de datos en PostgreSQL para el proyecto.
3.  Configura tus variables de entorno o edita el archivo `application.properties` con:
    *   Tus credenciales de PostgreSQL.
    *   Tu clave secreta para JWT.
    *   Tus API Keys de Stripe.
4.  Compila e inicia el servidor:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    *La API estará disponible en `http://localhost:8080` y la documentación de Swagger en `http://localhost:8080/swagger-ui.html`*

### 3. Configurar y levantar el Frontend
1.  Abre una nueva terminal y navega al directorio del frontend:
    ```bash
    cd ecom-frontend
    ```
2.  Instala las dependencias del proyecto:
    ```bash
    npm install
    ```
3.  Configura las variables de entorno necesarias (ej. la URL del backend y las claves públicas de Stripe) creando un archivo `.env`.
4.  Inicia el servidor de desarrollo:
    ```bash
    npm run dev
    ```
    *La aplicación web estará disponible en `http://localhost:5173`*

## 📸 Vistas de la Aplicación

> 💡 **Nota para el portfolio:** Reemplaza estos espacios con imágenes o GIFs reales de tu proyecto.

| Landing Page | Catálogo de Productos |
| :---: | :---: |
| <img src="https://via.placeholder.com/400x250.png?text=Landing+Page+Screenshot" alt="Landing Page" width="100%"> | <img src="https://via.placeholder.com/400x250.png?text=Catalogo+Screenshot" alt="Catálogo" width="100%"> |

| Carrito de Compras | Panel de Administración |
| :---: | :---: |
| <img src="https://via.placeholder.com/400x250.png?text=Carrito+Screenshot" alt="Carrito" width="100%"> | <img src="https://via.placeholder.com/400x250.png?text=Admin+Dashboard+Screenshot" alt="Dashboard" width="100%"> |

## 🎯 Conclusiones y Aprendizajes

La creación de este eCommerce me permitió afianzar conocimientos avanzados en el desarrollo Full Stack, específicamente logrando:
*   Diseñar una **arquitectura RESTful** limpia y mantenible separando responsabilidades en capas.
*   Implementar **flujos de seguridad robustos** protegiendo endpoints y vistas del lado del cliente.
*   Manejar un **estado global complejo** en el frontend de forma predecible con Redux Toolkit.
*   Integrar de forma exitosa plataformas de terceros como la pasarela de pagos de **Stripe**.
*   Prepararme para desafíos mayores en arquitecturas empresariales, microservicios y despliegues en la nube.

---

<p align="center">
  Desarrollado por <a href="https://github.com/tu-usuario">Tu Nombre</a> 👨‍💻
  <br/>
  Si te gustó el proyecto no olvides dejar una ⭐
</p>
