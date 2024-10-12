# PGEC (Planificación y Gestión de Eventos Campanero)


**PGEC** es una aplicación web diseñada para la venta y planificación de entradas de eventos culturales y de ocio. Su objetivo es facilitar la gestión de eventos y ofrecer una experiencia óptima a diferentes tipos de usuarios.

Acceso a la aplicación: https://pgec-app-28677546629a.herokuapp.com/

## 🎯 Objetivos

- Ofrecer una plataforma intuitiva y fácil de usar para la compra y planificación de eventos.
- Permitir a los administradores gestionar de manera eficiente el contenido y las estadísticas de ventas.
- Facilitar la devolución de entradas y la gestión de compras para los clientes registrados.

## 🚀 Funcionalidades principales

| Rol         | Funcionalidades                                                                                                                                             |
|-------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Visitante** | Ver eventos disponibles y detalles de los eventos sin necesidad de registro.                                                                               |
| **Cliente**   | Registrar e iniciar sesión, comprar entradas, ver historial de compras, devolver entradas y modificar datos personales.                                     |
| **Administrador** | Crear, editar o eliminar eventos y ver estadísticas de ventas.                                                                                          |

## 🛠️ Tecnologías utilizadas

### Frontend
- **Bootstrap**: Proporciona una interfaz moderna, responsive y adaptable a diferentes dispositivos.
- **HTML, CSS, JavaScript**: Construcción de la interfaz de usuario.

### Backend
- **Spring Boot**: Framework basado en Java para el desarrollo de aplicaciones robustas y escalables.
- **Spring MVC**: Implementa el patrón Modelo-Vista-Controlador, separando la lógica de negocio de la presentación.
- **Mustache**: Motor de plantillas para la generación de vistas dinámicas.

### Base de datos
- **MySQL**: Almacenamiento de datos seguro y consistente, garantizando la integridad de la información.

### Herramientas adicionales
- **Maven**: Gestión de dependencias y ciclo de vida de la aplicación.
- **Heroku**: Despliegue de la aplicación en la nube, permitiendo el acceso desde cualquier dispositivo.

## 📐 Arquitectura

PGEC sigue el patrón de diseño **MVC** (Modelo-Vista-Controlador), que permite separar las diferentes capas de la aplicación de la siguiente manera:

- **Modelo**: Gestión de datos y lógica de negocio (interacción con la base de datos).
- **Vista**: Interfaz de usuario donde los datos se presentan al usuario.
- **Controlador**: Actúa como intermediario entre la vista y el modelo, procesando las entradas del usuario y actualizando el modelo.

### Diagrama de la arquitectura MVC
```mermaid
graph TD
    A[Usuario] -->|Solicita página o acción| B[Controlador]
    B -->|Solicita o actualiza datos| C[Modelo]
    C -->|Consulta/Guarda| D[Base de Datos]
    D -->|Datos de eventos, tickets, usuarios| C
    B -->|Envía datos al usuario| E[Vista]
    E -->|Muestra eventos o confirma acciones| A

    subgraph Componentes MVC
        C[Modelo] -->|Clases: Event, Ticket, User| F[Lógica de negocio]
        E[Vista] -->|HTML, CSS, Mustache| G[Interfaz de usuario]
        B[Controlador] -->|EventController, TicketController, UserController| H[Procesa acciones del usuario]
    end

