# Next Demo Slice

This is the smallest next implementation slice that would make the repository
stronger in a technical interview.

## Goal

Build a producer-consumer lab with retry and dead-letter behavior.

## Scope

- Publish a message with a stable contract.
- Consume and acknowledge successful messages.
- Reject failed messages into a DLQ after retries.
- Document replay and idempotency considerations.

## Interview Value

This slice proves asynchronous system thinking by showing how the design handles
failure, recovery, and duplicate delivery.
