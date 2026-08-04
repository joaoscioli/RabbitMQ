# Technical Depth Map

This map connects the repository to the skills a technical reviewer may look for.

## Messaging Architecture

- Exchanges, queues, routing keys, and topology naming are documented clearly.
- Producer and consumer concerns are separated.
- Async design is connected to backend business workflows.

## Reliability

- Retry limits, DLQs, replay, idempotency, and backpressure are documented.
- Failure modes are discussed before adding more implementation volume.
- Producer safeguards make unroutable messages visible.

## Operations

- Local RabbitMQ setup supports reproducible learning.
- Observability notes explain what to monitor in message-driven systems.

## Interview Use

Start with DLQ and idempotency, then explain how duplicate and poison messages
change system design.
