# Technical Risks

This file captures the main risks in RabbitMQ-based systems and how the project
plans to reduce them.

## Risks

- Consumers can process duplicate messages.
- Failed messages can be lost without DLQ handling.
- Retry storms can overload downstream services.
- Message contracts can drift between producers and consumers.

## Mitigations

- Design consumers to be idempotent.
- Route exhausted failures to a dead-letter queue.
- Use bounded retry policies with backoff.
- Document message schemas and versioning rules.

## Interview Angle

The strongest discussion is how asynchronous systems behave under failure:
duplicates, retries, ordering, backpressure, and recovery.
