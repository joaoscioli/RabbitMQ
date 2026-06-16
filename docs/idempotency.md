# Idempotency In Message Consumers

Idempotency means that processing the same message more than once does not
create duplicate side effects.

## Why It Matters

Message-driven systems can deliver a message more than once.

This can happen after:

- consumer timeout;
- retry;
- redelivery after connection loss;
- manual replay from a DLQ;
- partial failure after an external call.

## Techniques

Useful idempotency techniques:

- store processed event ids;
- use unique constraints;
- check current state before updating;
- use idempotency keys for external APIs;
- make handlers safe for duplicate delivery.

## Example

For `billing.subscription.created`, store the `eventId` after successful
processing.

If the same event arrives again, the consumer can skip it safely.

## Interview Talking Points

- At-least-once delivery means duplicates are possible.
- Retry and replay require idempotent consumers.
- Unique constraints can protect important side effects.
- Idempotency should be designed before production message replay.
