# Technical Scope

This file defines what the project is meant to prove and what it does not claim yet.

## In Scope

- RabbitMQ messaging fundamentals.
- Routing, retries, DLQs, replay, and idempotency.
- Message contract thinking.
- Operational failure-mode documentation.

## Out of Scope for Now

- Large-scale broker clustering.
- Exactly-once delivery guarantees.
- Full production monitoring stack.
- Messaging where synchronous flow would be simpler.

## Scope Statement

The project is scoped to prove asynchronous architecture judgment. The next
milestone is a runnable producer-consumer lab with failure handling and DLQ
behavior.
