# Tough Interview Questions

Use these questions to prepare direct, technical answers.

## Does RabbitMQ make a system reliable?

No. RabbitMQ provides messaging primitives. Reliability comes from explicit
routing, acknowledgements, retries, DLQs, idempotency, replay strategy, and
observability.

## What is the danger of retries?

Retries can amplify failures, overload dependencies, and hide poison messages
unless they are bounded and connected to dead-letter handling.

## When should you avoid messaging?

Avoid it when synchronous feedback is required, the workflow is simple, or the
team cannot operate the added infrastructure.

## What would you defend in a code review?

Designing duplicate-message and failure behavior before adding more consumers.
