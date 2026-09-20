# Interview Defense Notes

Use these notes when a reviewer challenges the project scope or design choices.

## Likely Challenge

"Why use messaging instead of a direct synchronous call?"

## Defense

Messaging is justified when decoupling, retries, recovery, or independent
consumer evolution matter. The repository should defend RabbitMQ through failure
handling, idempotency, DLQs, replay, and contract versioning.

## Evidence To Show

- `docs/dead-letter-queue.md`
- `docs/technical-risks.md`
- `docs/acceptance-criteria.md`

## Senior Signal

The decision shows that asynchronous architecture is about failure behavior and
operational recovery, not just queue usage.
