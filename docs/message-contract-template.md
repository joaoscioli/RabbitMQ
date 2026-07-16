# Message Contract Template

Use this template to document events exchanged through RabbitMQ.

## Event Name

```text
billing.subscription.created
```

## Routing

- exchange:
- exchange type:
- routing key:
- queue:
- dead-letter queue:

## Producer

- service:
- owner:
- when published:

## Consumer

- service:
- owner:
- processing responsibility:

## Payload

```json
{
  "eventId": "evt-123",
  "occurredAt": "2026-06-14T12:00:00Z",
  "organizationSlug": "acme"
}
```

## Required Fields

- `eventId`
- `occurredAt`
- `organizationSlug`

## Versioning

- current version:
- backward compatibility notes:
- deprecated fields:

## Failure Handling

- retryable failures:
- permanent failures:
- DLQ destination:
- replay rules:

## Interview Talking Points

- Message contracts reduce producer-consumer drift.
- Routing belongs in documentation, not only code.
- Versioning matters because producers and consumers evolve independently.
- Replay rules should consider idempotency.

## Questions This Template Can Answer

- Who owns a message contract when multiple services depend on it?
- What happens when a message cannot be routed?
- Which failures should be retried and which should go to a DLQ?
- How does idempotency make replay safer?
