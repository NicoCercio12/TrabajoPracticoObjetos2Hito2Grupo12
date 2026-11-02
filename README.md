# TrabajoPracticoObjetos2Hito2Grupo12

 # Epicentro Gourmet

**Trabajo Práctico de Orientación a Objetos II · Hito 2 · Grupo 12**

Epicentro Gourmet es una aplicación web para gestionar un predio ferial gastronómico. Administra los festivales culinarios que se realizan en el predio, las unidades de venta (food trucks y puestos desarmables) que participan en cada uno, el personal que trabaja en ellas, los platos y pedidos que se venden, y los reportes económicos de cada evento: recaudación, canon, sueldos y rentabilidad.

El sistema distingue cuatro tipos de usuario, cada uno con su propia vista y permisos: el **administrador** que supervisa todo el predio, el **responsable** que gestiona sus unidades y su personal, y los **empleados** (cajero y cocinero) que consultan su información y operan su caja.

---

## Descripción del proyecto

### ¿Qué problema resuelve?
Organizar un festival gastronómico implica coordinar muchas partes a la vez: qué unidades participan, quién trabaja en cada una, cuánto debe pagar cada puesto por ocupar su lugar (canon), cuánto cobra cada empleado y cuánto gana realmente cada unidad una vez descontados costos, sueldos y canon. Epicentro Gourmet centraliza esa información y calcula automáticamente los montos, para que la gestión sea trazable y cada persona vea solo lo que le corresponde.

### Roles y qué puede hacer cada uno

| Rol | Qué puede hacer |
|---|---|
| **Visitante** (sin sesión) | Ver la cartelera de festivales y las unidades que participan en cada uno. Crear una cuenta. |
| **Pendiente** | Cuenta recién creada, a la espera de que un administrador la apruebe. No accede a funciones del sistema. |
| **Administrador** | Ver todos los empleados y todas las unidades de venta, y consultar los reportes por festival: ranking de unidades por recaudación y unidades con mayor canon. |
| **Responsable** | Registrar sus unidades de venta, dar de alta, modificar, dar de baja y reactivar a su personal, cargar y consultar platos, y ver el canon, la liquidación de haberes y los reportes de sus unidades. |
| **Cajero** | Ver su perfil, sus festivales y su sueldo, descargar su recibo en PDF y cerrar la caja al finalizar el día. |
| **Cocinero** | Ver su perfil, los festivales en los que trabaja y su sueldo, y descargar su recibo en PDF. |

### Flujo de una cuenta
1. Una persona se registra en **Crear cuenta** con su email y contraseña. La cuenta queda en estado *pendiente*.
2. Un **administrador** aprueba la cuenta asignándole el rol de responsable.
3. El **responsable** completa sus datos personales y registra su primera unidad de venta (food truck o puesto desarmable). A partir de ahí puede registrar más unidades.
4. El responsable da de alta a su personal. Cada empleado recibe por **email** su usuario y una contraseña generada aleatoriamente para iniciar sesión.
5. Cada rol trabaja con las funciones de su perfil.

### Reglas de negocio principales
- **Canon de una unidad.** Food truck: `superficie × $500`, más `$2.000` si requiere conexión eléctrica. Puesto desarmable: `superficie × $500 − tiempo de montaje × $10`.
- **Sueldo del personal.** Sueldo base de `$100.000` más `$5.000` por cada año de antigüedad, ajustado según la duración del festival y el tipo de empleado.
- **Rentabilidad neta.** Pedidos totales menos costos de los platos, menos los sueldos y el canon, para un festival o entre dos fechas.
- **Baja lógica.** Los empleados no se eliminan: se dan de baja y pueden reactivarse. Los listados solo traen entidades activas.
- **Cierre de caja.** Cada cajero cierra su caja una sola vez por día.
- **Validaciones de carga.** DNI único, mayoría de edad, email único, código de unidad único, patente con formato válido, superficie positiva, entre otras.

---

## Casos de uso por integrante

| N° | Caso de uso | Responsable |
|---|---|---|
| 1 | Top 3 platos más vendidos de una unidad en un festival | Lucas La Rosa |
| 2 | Día de mayor facturación de la unidad en el festival | Lucas La Rosa |
| 3 | Cálculo de rentabilidad neta entre dos fechas | Lucas La Rosa |
| 4 | Ver unidades de venta de un festival | Lucas La Rosa |
| 5 | Cálculo de canon | Ariel Contreras |
| 6 | Liquidación de haberes | Ariel Contreras |
| 7 | Reporte de recaudación | Ariel Contreras |
| 8 | Plato estrella | Ariel Contreras |
| 9 | Reasignar empleado a otra unidad de venta | Ariel Contreras |
| 10 | Ranking de unidades por recaudación | Fabricio Maldonado |
| 11 | Dar de alta un empleado | Matías Funes |
| 12 | Dar de baja un empleado | Matías Funes |
| 13 | Reactivar un empleado | Matías Funes |
| 14 | Dar de alta un plato | Matías Funes |
| 15 | Listar platos de las unidades propias | Matías Funes |
| 16 | Modificar datos de un empleado | Matías Funes |
| 17 | Buscar y filtrar empleados activos | Matías Funes |
| 18 | Consultar platos vendidos por unidad entre fechas | Matías Funes |
| 19 | Login con seguridad | Nicolás Cerciosimo |
| 20 | Cerrar caja | Nicolás Cerciosimo |
| 21 | Empleado con festival activo | Nicolás Cerciosimo |
| 22 | Registrar unidad como responsable | Nicolás Cerciosimo |
| 23 | Rentabilidad neta | Fabricio Maldonado |
| 24 | Rentabilidad neta entre dos fechas | Fabricio Maldonado |
| 25 | Unidades con mayor canon (`ReporteMayoresCanonDTO`) | Fabricio Maldonado |
| 26 | Descargar sueldo por PDF | Nicolás Cerciosimo |

---

## Arquitectura y diseño

La aplicación sigue una arquitectura en capas con el patrón MVC de Spring:

| Capa | Paquete | Responsabilidad |
|---|---|---|
| Controladores | `controllers` | Reciben las peticiones web, validan la entrada y eligen la vista. |
| Servicios | `services` y `services.implementation` | Lógica de aplicación y reglas de negocio. Cada servicio tiene su interfaz (`I...Service`). |
| Repositorios | `repositories` | Acceso a datos con Spring Data JPA. |
| Entidades | `entities` | Modelo de dominio persistente. |
| DTOs | `dtos` | Objetos de transferencia para formularios y reportes (no persisten). |
| Vistas | `resources/templates` | Plantillas Thymeleaf con Bootstrap. |
| Configuración | `configuration` | Seguridad (Spring Security) y beans. |

### Modelo de dominio

- **`Festival`**: nombre, temporada, fecha de inicio y fin. Se relaciona con las unidades que participan, con los pedidos y con sus costos.
- **`UnidadDeVenta`** (clase base) con dos especializaciones: **`FoodTruck`** (patente, requiere conexión eléctrica) y **`PuestoDesarmable`** (cantidad de carpas, tiempo de montaje). Cada una calcula su propio canon (polimorfismo).
- **`Empleado`** (clase base) con dos especializaciones, mapeadas con herencia `JOINED`: **`Cajero`** (turno, cierres de caja) y **`Cocinero`** (rol de cocina). Cada tipo calcula su propio sueldo.
- **`CierreCaja`**: fecha y total recaudado de un cajero. El cajero lo crea con `cerrarCaja(fecha, total)` y evita duplicados.
- **`User` y `UserRole`**: cuentas de acceso y sus roles.
- Entidades de ventas: **`Plato`**, **`Pedido`** y **`Costo`**.

---

## Tecnologías

- **Java 25**
- **Spring Boot 4.1.1**: Web MVC, Data JPA, Security, Validation, Mail
- **Hibernate** como implementación de JPA
- **Thymeleaf** (con Spring Security) y **Bootstrap 5** para las vistas
- **MySQL** como base de datos (`db_grupo_12_hito_2`)
- **OpenPDF** para generar los recibos de sueldo
- **Lombok** y **ModelMapper**
- **Maven** como gestor de dependencias

---

## Cómo ejecutar el proyecto

### Requisitos
- JDK 25
- MySQL en ejecución, con una base de datos llamada `db_grupo_12_hito_2`
- Maven (o el wrapper incluido `mvnw`)

### Configuración
Las credenciales **no se versionan**. El archivo `src/main/resources/application.yml` está ignorado por Git y cada integrante crea el suyo en local. Se leen de variables de entorno:

| Variable | Uso |
|---|---|
| `DB_USER` | Usuario de la base de datos (por defecto `root`) |
| `DB_PASSWORD` | Contraseña de la base de datos |
| `MAIL_USER` | Cuenta que envía los emails (por ejemplo, las credenciales de nuevos empleados) |
| `MAIL_PASSWORD` | Contraseña de aplicación de esa cuenta |

Ejemplo de `application.yml` local:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_grupo_12_hito_2?serverTimezone=America/Argentina/Buenos_Aires
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:}
  jpa:
    hibernate:
      ddl-auto: update
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USER:}
    password: ${MAIL_PASSWORD:}
    properties:
      mail.smtp.auth: true
      mail.smtp.starttls.enable: true
server:
  port: ${PORT:8080}
```

### Ejecución
```bash
./mvnw spring-boot:run
```
La aplicación queda disponible en `http://localhost:8080`.

En IntelliJ, las variables de entorno se definen en la configuración de ejecución (*Run → Edit Configurations → Environment variables*).

## Integrantes

- Lucas La Rosa
- Ariel Contreras
- Fabricio Maldonado
- Matías Funes
- Nicolás Cerciosimo
