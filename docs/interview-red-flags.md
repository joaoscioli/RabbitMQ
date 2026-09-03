# Interview Red Flags

Use this file to avoid weak signals during a technical interview.

## Avoid Saying

- "RabbitMQ makes the system reliable."
- "Messages are processed exactly once."
- "Retries solve temporary failures."
- "A DLQ is enough observability."

## Say Instead

- RabbitMQ provides messaging primitives; reliability comes from system design.
- Consumers should expect duplicate delivery and handle idempotency.
- Retries must be bounded and connected to dead-letter handling.
- Queue depth, retry volume, DLQ growth, and processing latency all matter.
