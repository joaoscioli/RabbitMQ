# Reviewer Guide

This guide helps a technical reviewer understand the messaging engineering
signals demonstrated by this repository.

## What To Review First

1. `README.md` for repository purpose.
2. `docs/messaging-fundamentals.md` for core concepts.
3. `docs/retry-strategy.md` and `docs/dead-letter-queue.md` for failure design.
4. `docs/idempotency.md` for duplicate-message handling.
5. `spring-rabbitMQ/` for the Spring RabbitMQ example.
6. `SenderConfig` for producer-side publishing safeguards.

## Strong Signals

- Messaging examples include failure handling, replay, idempotency, and
  observability concerns.
- Documentation explains why queue design affects reliability.
- Local setup is separated from production assumptions.
- The project treats consumers as part of a distributed system boundary.
- Producer configuration avoids silent unroutable-message failures.

## Interview Talking Points

- Explain when async messaging helps and when it adds unnecessary complexity.
- Discuss retries, dead letters, and idempotency together.
- Show how observability changes the way messaging systems are operated.
- Explain why publisher guarantees matter before adding more consumers.
