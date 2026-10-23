# Backend Cloud Native - RabbitMQ

Microservicios Spring Boot 4 (Java 21) con RabbitMQ, JWT (Keycloak) y PostgreSQL (Neon).

## Servicios
| Servicio | Puerto | Rol |
|---|---|---|
| ms-pedidos | 8081 | Productor, BD, JWT |
| ms-notificaciones | 8082 | Consumidor con ACK manual, reintentos y DLQ |
| ms-admin-rabbit | 8083 | Administracion de colas, exchanges y bindings |

## Requisitos
Java 21, Docker, cuenta en Neon (PostgreSQL).

## Puesta en marcha
1. Levantar RabbitMQ y Keycloak: `docker compose up -d`
   - RabbitMQ: http://localhost:15672 (guest/guest)
   - Keycloak: http://localhost:8080 (admin/admin)
2. En Keycloak crear el realm `cloudnative`, el cliente publico `angular-app`
   (redirect `http://localhost:4200/*`, web origins `http://localhost:4200`, PKCE S256)
   y un usuario de prueba con perfil completo (email, nombre, apellido).
   Alternativa: importar `keycloak/cloudnative-realm.json` si existe.
3. Crear `ms-pedidos/src/main/resources/application-local.yml` (no se sube a Git):
   ```yaml
   spring:
     datasource:
       password: TU_PASSWORD_DE_NEON
   ```
4. Arrancar cada servicio desde su carpeta:
   - `./mvnw spring-boot:run` (ms-notificaciones y ms-admin-rabbit)
   - `./mvnw spring-boot:run -Dspring-boot.run.profiles=local` (ms-pedidos)

## Mensajeria
Exchange `tareas.exchange` (direct) -> cola `notificaciones.queue` (routing key `notificacion.enviar`).
Fallos: `tareas.dlx` -> `notificaciones.dlq`. Los nombres estan centralizados en `application.yml`.

## API
- ms-pedidos: `POST /api/pedidos`, `GET /api/pedidos` (JWT)
- ms-admin-rabbit: `POST/DELETE /api/queues`, `/api/exchanges`, `/api/bindings` (JWT)
