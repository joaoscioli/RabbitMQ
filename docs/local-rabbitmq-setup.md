# Local RabbitMQ Setup

This document describes a local setup direction for RabbitMQ examples.

## Docker Compose Service

Start the broker from the repository root:

```bash
docker compose up -d
```

The service is defined in `docker-compose.yml`:

```yaml
services:
  rabbitmq:
    image: rabbitmq:3-management
    container_name: rabbitmq-messaging-lab
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      RABBITMQ_DEFAULT_USER: guest
      RABBITMQ_DEFAULT_PASS: guest
```

## Useful URLs

- AMQP port: `localhost:5672`
- Management UI: `http://localhost:15672`
- Default username: `guest`
- Default password: `guest`

## Local Development Notes

- Use local credentials only for development.
- Keep queues and exchanges named clearly.
- Prefer durable queues for reliability examples.
- Document routing keys before writing consumers.
- Reset local broker state when testing topology changes.

## Interview Talking Points

- Local broker setup makes messaging examples reproducible.
- The management UI helps inspect queues, exchanges, and bindings.
- Development credentials must not be reused in production.
- Topology should be documented before consumers depend on it.
