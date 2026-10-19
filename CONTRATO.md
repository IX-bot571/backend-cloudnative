# Contrato del sistema

## Microservicios
| Servicio | Puerto | Rol |
|---|---|---|
| ms-pedidos | 8081 | Productor, BD, JWT |
| ms-notificaciones | 8082 | Consumidor, DLQ |
| ms-admin-rabbit | 8083 | Administrador de RabbitMQ |

## Mensajería
| Elemento | Nombre |
|---|---|
| Exchange principal (direct) | tareas.exchange |
| Cola | notificaciones.queue |
| Routing key | notificacion.enviar |
| Exchange de fallos (direct) | tareas.dlx |
| DLQ | notificaciones.dlq |
| Routing key DLQ | notificacion.fallida |

## API ms-pedidos
- POST /api/pedidos
- GET /api/pedidos

## API ms-admin-rabbit
- POST /api/queues, DELETE /api/queues/{name}
- POST /api/exchanges, DELETE /api/exchanges/{name}
- POST /api/bindings, DELETE /api/bindings

## DTOs (por definir al implementar)
- PedidoRequest: cliente, producto, cantidad
- NotificacionMensaje: pedidoId, cliente, mensaje
