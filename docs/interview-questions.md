# Interview Questions

Use these questions to prepare a messaging architecture discussion.

## When is RabbitMQ a good choice?

RabbitMQ is useful when work should be processed asynchronously, routed by
intent, retried safely, or decoupled from the request path.

## What makes messaging reliable?

Reliability comes from explicit routing, acknowledgements, bounded retries,
dead-letter handling, idempotent consumers, and visibility into failed or
unroutable messages.

## What is the risk of adding queues too early?

Queues add operational complexity. If the workflow is simple and synchronous
feedback matters, a direct call can be easier to operate and debug.

## What is the senior-level signal?

The senior signal is explaining failure modes: duplicate messages, poison
messages, backpressure, replay, dead-letter queues, and producer-side
safeguards.
