# Decision Log

This log summarizes important technical choices in a review-friendly format.

## Decisions

| Decision | Reason | Trade-off |
| --- | --- | --- |
| Design around at-least-once delivery. | Matches realistic broker behavior and duplicate-message risk. | Consumers need idempotency strategy. |
| Make DLQ handling explicit. | Prevents poison messages from being retried forever. | Adds operational review work. |
| Document retry and backpressure behavior. | Helps reviewers see failure handling before scaling consumers. | More design work is needed before adding features. |

## Interview Use

Use this file to explain how messaging reliability comes from system design, not
from RabbitMQ alone.
