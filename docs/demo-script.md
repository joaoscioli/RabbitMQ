# Demo Script

Use this short script to present the repository in a technical interview.

## 3-Minute Walkthrough

1. Start with the goal: RabbitMQ concepts for reliable backend messaging.
2. Show routing, retry, dead-letter, idempotency, and producer safeguard docs.
3. Explain why producer-side failure visibility matters before adding more
   consumers.
4. Close with the next step: publisher return handling for mandatory messages.

## What To Emphasize

- Messaging reliability is more than sending and consuming.
- Routing and contracts should be documented.
- Replay is safer when idempotency is designed first.

## Before The Interview

- Be ready to explain exchange, routing key, queue, and DLQ together.
- Prepare one duplicate-message scenario.
- Know why producer-side unroutable messages matter.

## Evidence To Open

- `spring-rabbitMQ/src/main/java/com/example/springrabbitMQ/SenderConfig.java`
- `spring-rabbitMQ/src/main/java/com/example/springrabbitMQ/QueueSender.java`
- `docs/idempotency.md`
- `docs/dead-letter-queue.md`

## Avoid Saying

- "RabbitMQ makes the system reliable." Instead, explain retries, DLQs, and
  idempotency.
- "Async is always better." Instead, explain when messaging adds complexity.
