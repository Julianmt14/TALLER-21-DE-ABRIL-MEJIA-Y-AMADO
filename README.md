# bookstore-api

API REST para una librería en línea construida con Spring Boot 3, JWT, JPA y arquitectura por capas.

## Tecnologías

- Java 17
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- H2 para desarrollo
- PostgreSQL para producción
- Swagger / OpenAPI
- Maven y Gradle Wrapper

## Requisitos

- Java 17+
- Maven 3.8+ opcional
- O usar `gradlew.bat`

## Ejecución

### Con Gradle Wrapper

```powershell
.\gradlew.bat bootRun
```

### Con Maven

```powershell
mvn spring-boot:run
```

## Variables de entorno

- `JWT_SECRET`
- `JWT_EXPIRATION`
- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

## Perfiles

- `dev`: H2 en memoria, datos iniciales y consola H2
- `prod`: PostgreSQL

## URLs útiles

- API base: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- H2 Console: `http://localhost:8080/api/v1/h2-console`

## Credenciales iniciales en dev

- Admin
  - email: `admin@bookstore.com`
  - password: `Admin1234`
- User
  - email: `user@bookstore.com`
  - password: `User12345`

## Estructura del proyecto

```text
src/main/java/com/taller/bookstore
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
│   ├── custom
│   └── handler
├── mapper
├── repository
├── security
├── service
└── impl
```

## Flujo básico JWT

1. Registrar usuario en `POST /auth/register`
2. Autenticarse en `POST /auth/login`
3. Copiar `token`
4. Enviar `Authorization: Bearer <token>`
5. Consumir endpoints protegidos

## Reglas de acceso

- Públicos:
  - `POST /auth/register`
  - `POST /auth/login`
  - `GET /books/**`
  - `GET /authors/**`
  - `GET /categories/**`
- `ADMIN`:
  - CRUD de libros, autores y categorías
  - `GET /orders`
  - `PUT /orders/{id}/confirm`
- `USER`:
  - `POST /orders`
  - `GET /orders/my`
- `USER` y `ADMIN`:
  - `GET /orders/{id}`
  - `PUT /orders/{id}/cancel`

## Probar con Postman

Importa [postman/bookstore-api.postman_collection.json](postman/bookstore-api.postman_collection.json).

## Ejemplos rápidos

### Login

```json
{
  "email": "admin@bookstore.com",
  "password": "Admin1234"
}
```

### Crear libro

```json
{
  "title": "Domain-Driven Design",
  "description": "Strategic design and tactical patterns",
  "price": 149.9,
  "stock": 12,
  "isbn": "9780321125217",
  "authorId": 1,
  "categoryIds": [1, 2]
}
```

### Crear pedido

```json
{
  "items": [
    {
      "bookId": 1,
      "quantity": 2
    }
  ]
}
```

## Git sugerido

- `main`
- `develop`
- `feature/auth-module`
- `feature/book-catalog`
- `feature/order-management`
- `feature/author-category`

### Ejemplos de commits

- `feat: add JWT authentication filter`
- `fix: correct price validation in BookRequest`
- `refactor: extract order total calculation to service`
- `docs: add endpoint documentation in AuthController`
- `test: add unit tests for OrderMapper`
- `chore: update application.yml with JWT config`
