# Implementation Priority

This file defines the next implementation order for turning the repository into
a stronger messaging portfolio.

## P1: Producer And Consumer Flow

- Add a small producer with a stable message contract.
- Add a consumer that acknowledges successful processing.
- Document the exchange, queue, and routing key.

## P2: Failure Handling

- Add retry behavior with a bounded policy.
- Route exhausted failures to a dead-letter queue.
- Document replay and poison-message handling.

## P3: Operational Concerns

- Add idempotency notes to the consumer.
- Document backpressure and monitoring signals.
- Prepare a local Docker Compose demo.

## Why This Order

The repository should prove reliable messaging behavior before scaling topology.
Failure handling is the real interview signal for asynchronous systems.
