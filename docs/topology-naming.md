# Topology Naming

Consistent names make RabbitMQ systems easier to review and operate.

## Goal

Queues, exchanges, and routing keys should describe business intent. A reviewer
should understand the message flow without reading every consumer.

## Suggested Convention

Use this shape:

```text
<bounded-context>.<event-or-command>.<version>
```

Examples:

- `billing.invoice-created.v1`
- `billing.payment-failed.v1`
- `subscription.subscription-canceled.v1`

## Queue Names

Queue names should describe the consumer responsibility:

```text
<service>.<purpose>.queue
```

Examples:

- `notification.invoice-email.queue`
- `audit.subscription-events.queue`
- `billing.payment-retry.queue`

## Dead-Letter Names

Dead-letter queues should be explicit:

```text
<queue-name>.dlq
```

Example:

```text
notification.invoice-email.queue.dlq
```

## Portfolio Signal

Naming is not cosmetic in messaging systems. Clear topology names reduce
operational confusion and make asynchronous flows easier to debug.
