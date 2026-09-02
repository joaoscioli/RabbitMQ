# Reviewer FAQ

## What should I review first?

Start with the README, messaging fundamentals, retry strategy, DLQ notes, and
idempotency guide.

## What is the main technical signal?

The project explains asynchronous reliability through contracts, retries,
acknowledgements, DLQs, idempotency, and observability.

## What is intentionally unfinished?

A runnable producer-consumer lab and persistence-backed idempotency example can
still make the repository stronger.

## What should we discuss in an interview?

Discuss at-least-once delivery, duplicate messages, poison messages, replay,
backpressure, and when messaging is worth the complexity.
