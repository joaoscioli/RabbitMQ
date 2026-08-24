# Architecture Review Checklist

Use this checklist to review the project as a messaging architecture exercise.

## Messaging Boundary

- Producers and consumers communicate through explicit message contracts.
- Routing decisions are documented through exchange and queue design.
- Consumers do not assume exactly-once delivery.

## Failure Handling

- Acknowledgements, retries, and dead-letter flows are intentional.
- Idempotency is considered before adding more consumers.
- Poison messages have a reviewable path instead of being retried forever.

## Operational Review

- Queue depth, retry volume, and DLQ volume can be observed.
- Replay strategy is separated from normal processing.
- Backpressure is treated as a system behavior, not an afterthought.

## Interview Defense

Be ready to explain what RabbitMQ gives you, what it does not give you, and how
you design reliability around those boundaries.
