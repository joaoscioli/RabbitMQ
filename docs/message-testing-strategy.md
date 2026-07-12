# Message Testing Strategy

Message-driven systems need tests at different levels.

## Unit Tests

Use unit tests for:

- message payload validation;
- consumer business logic;
- idempotency decisions;
- retry classification;
- mapping from event to domain action.

These tests should not require a real broker.

## Integration Tests

Use integration tests for:

- exchange and queue declarations;
- routing key behavior;
- serialization and deserialization;
- listener wiring;
- dead-letter configuration.

These tests may use Testcontainers with RabbitMQ.

## Contract Checks

Message contracts should document:

- event name;
- routing key;
- payload fields;
- required fields;
- versioning approach;
- producer and consumer ownership.

## Publishing Checks

Producer configuration should make unroutable messages visible. Mandatory
publishing is one option because it allows the application to detect when a
message cannot be routed to a queue.

## Interview Talking Points

- Not every messaging test needs a broker.
- Unit tests protect handler behavior.
- Integration tests protect wiring and broker assumptions.
- Message contracts reduce producer-consumer drift.
- Producers should avoid silently dropping unroutable messages.
