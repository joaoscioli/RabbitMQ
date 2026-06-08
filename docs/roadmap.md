# RabbitMQ Messaging Lab Roadmap

This roadmap defines how this repository will evolve from legacy notes into a
professional backend messaging lab.

## Phase 1: Repository Foundation

- Create a professional README.
- Define the repository role in the portfolio.
- Preserve existing learning history.
- Add a clear roadmap.

## Phase 2: Messaging Fundamentals

- Document queues, exchanges, bindings, and routing keys. Done.
- Explain direct, fanout, topic, and headers exchanges. Done.
- Add simple diagrams for producer, exchange, queue, and consumer flow. Done.

## Phase 3: Spring Boot Producer and Consumer

- Reorganize the existing Spring Boot example.
- Add a message producer with a clear domain event.
- Add a consumer with focused responsibilities.
- Document local RabbitMQ setup.

## Phase 4: Reliability Patterns

- Add manual acknowledgement notes.
- Add retry strategy documentation.
- Add dead-letter queue example.
- Explain idempotency and duplicate message handling.

## Phase 5: Testing and Observability

- Add tests for message serialization and handler behavior.
- Document what should be tested without a real broker.
- Document what should be tested with integration tests.
- Add logging and metrics notes for message-driven systems.
