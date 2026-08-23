# Senior Review Notes

These notes help a technical interviewer review the project quickly.

## What to Look For

- Explicit thinking around acknowledgements, retries, DLQs, and idempotency.
- Documentation that separates messaging primitives from reliability guarantees.
- Failure-path examples that show what happens when processing cannot continue.
- Clear operational vocabulary around queues, exchanges, and consumers.

## Senior Signals

- RabbitMQ is not treated as a magic reliability layer.
- Retry behavior is bounded and connected to dead-letter handling.
- The project supports discussion about duplicate messages and replay strategy.

## Discussion Angle

Use this project to discuss asynchronous architecture, failure handling, and the
cost of introducing messaging into a backend system.
