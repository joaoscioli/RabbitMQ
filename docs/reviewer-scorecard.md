# Reviewer Scorecard

Use this scorecard to review the repository quickly during portfolio screening.

## Strong Signals

- Messaging is explained through reliability, not only basic publishing.
- Retry, dead-letter, idempotency, replay, and backpressure are documented.
- Producer-side safeguards are treated as part of system correctness.
- The repository connects RabbitMQ concepts to backend failure modes.

## Evidence

- `docs/dead-letter-queue.md`
- `docs/idempotency.md`
- `docs/retry-strategy.md`
- `spring-rabbitMQ/src/main/java/com/example/springrabbitMQ/SenderConfig.java`

## Next Senior Step

Add a small consumer example with explicit acknowledgement behavior and a test
that documents duplicate-message handling.
